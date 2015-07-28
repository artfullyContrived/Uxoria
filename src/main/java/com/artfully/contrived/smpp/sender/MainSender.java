package com.artfully.contrived.smpp.sender;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.jsmpp.bean.BindType;

import com.artfully.contrived.smpp.common.RebindParams;
import com.artfully.contrived.smpp.common.ShutdownHook;
import com.artfully.contrived.smpp.model.SMPP;
import com.artfully.contrived.smpp.receiver.module.SessionBinderFactory;
import com.artfully.contrived.smpp.receiver.subscribers.SessionStateSubscriber;
import com.artfully.contrived.smpp.sender.workers.MessageQueuePoller;
import com.artfully.contrived.util.PropertyUtils;
import com.artfully.contrived.util.Props;
import com.artfully.contrived.util.UxoriaUtils;
import com.github.rholder.retry.RetryerBuilder;
import com.google.common.eventbus.EventBus;

public class MainSender {

  /** The props. */
  private final Properties props;

  /** The Constant logger. */
  private static final Logger logger = Logger.getLogger(MainSender.class);

  /** The Constant eventBus. */
  private static EventBus eventBus;
  
  private static SessionBinderFactory sessionBinderFactory;

  /**
   * Instantiates a new receiver.
   */
  private MainSender() {

    Properties log4jprops = PropertyUtils.getPropertiesFromFile(Props.log4jPropertyFile.getFileName());
    PropertyConfigurator.configure(log4jprops);
    eventBus = new EventBus();
    props = PropertyUtils.getPropertiesFromFile(Props.receiverPropertyFile.getFileName());
    
    UxoriaUtils.initialize(props);
  }

  public static void main(String[] args) {
    MainSender sender = new MainSender();
    Collection<SMPP> smppBeans = sender.getTxSessions();

    ExecutorService bindExecutor = Executors.newCachedThreadPool();
    // TODO get num threads in properties file
    ExecutorService consumerService = Executors.newFixedThreadPool(1000);
    ScheduledExecutorService producerService = Executors.newScheduledThreadPool(5);
    RebindParams rebindParams = new RebindParams(sender.props);

    Collection<Future<SMPP>> futures = new LinkedList<Future<SMPP>>();

    eventBus.register(new SessionStateSubscriber());

    // get bindable sessions and pass them to binder
    try {
      Future<SMPP> future = null;
      for (SMPP smppBean : smppBeans) {
        future = bindExecutor.submit(RetryerBuilder
            .<SMPP> newBuilder()
            .withStopStrategy(rebindParams.getStopStrategy())
            .withWaitStrategy(rebindParams.getWaitStrategy())
            .retryIfExceptionOfType(rebindParams.getRetryException())
            .build()
            .wrap(sessionBinderFactory.create(smppBean)));

        futures.add(future);
      }

      SMPP smpp = null;
      for (Future<SMPP> session : futures) {
        smpp = session.get();

        producerService.scheduleWithFixedDelay(new MessageQueuePoller(smpp, consumerService), 0, 10, TimeUnit.SECONDS);

      }
    } catch (InterruptedException e) {
      logger.error(e, e);
    } catch (ExecutionException e) {
      logger.error(e, e);
    }

    Runtime.getRuntime().addShutdownHook(new ShutdownHook(smppBeans));
    bindExecutor.shutdown();
  }

  private Collection<SMPP> getTxSessions() {
    logger.debug("getTxSessions().");
    return UxoriaUtils.getSessions(BindType.BIND_TX);
  }
}
