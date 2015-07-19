/*
 * 
 */
package com.artfully.contrived.smpp.receiver;

import java.util.Collection;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.jsmpp.bean.BindType;

import com.artfully.contrived.smpp.common.RebindParams;
import com.artfully.contrived.smpp.model.SMPP;
import com.artfully.contrived.smpp.receiver.module.SessionBinderFactory;
import com.artfully.contrived.smpp.receiver.subscribers.ContentHandlingSubscriber;
import com.artfully.contrived.smpp.receiver.subscribers.DeliveryReportSubscriber;
import com.artfully.contrived.smpp.receiver.subscribers.MessageSavingSubscriber;
import com.artfully.contrived.smpp.receiver.subscribers.SessionStateSubscriber;
import com.artfully.contrived.util.PropertyUtils;
import com.artfully.contrived.util.Props;
import com.artfully.contrived.util.ShutdownHook;
import com.artfully.contrived.util.UxoriaUtils;
import com.github.rholder.retry.RetryerBuilder;
import com.google.common.eventbus.EventBus;

/**
 * The MainReceiver is the starting point for the receiver service.
 */
// TODO improve the naming of the classes to a way that makes sense
// TODO take care of CharEncoding everywhere
// TODO add RateLimiter in case we are flooded.
// TODO add metrics
public class MainReceiver {

  /** The Constant logger. */
  private static final Logger logger = Logger.getLogger(MainReceiver.class);

  private Properties props;

  private EventBus eventBus;

  private RebindParams rebindParams;

  private SessionBinderFactory sessionBinderFactory;

  @Inject
  public MainReceiver(Properties props, EventBus eventBus, RebindParams rebindParams,
      SessionBinderFactory sessionBinderFactory) {
    super();
    initializeLogger();
    System.out.println("props " + props);

    this.props = props;
    this.eventBus = eventBus;
    this.rebindParams = rebindParams;
    this.sessionBinderFactory = sessionBinderFactory;

  }

  private void initializeLogger() {
    Properties log4jprops = PropertyUtils.getPropertyFile(Props.log4jPropertyFile.getFileName());
    PropertyConfigurator.configure(log4jprops);
  }

  public void start() {
    UxoriaUtils.initialize(props);
    eventBus.register(new ContentHandlingSubscriber());
    eventBus.register(new MessageSavingSubscriber());
    eventBus.register(new DeliveryReportSubscriber());
    eventBus.register(new SessionStateSubscriber());

    Collection<SMPP> smppBeans = getRxSessions();
    ExecutorService executor = Executors.newFixedThreadPool(Integer.parseInt(props.getProperty("numThreads", "10")));

    // get bindable sessions and pass them to binder
    System.out.println("rebinds " + rebindParams.getStopStrategy());
    for (SMPP smppBean : smppBeans) {
      executor.submit(RetryerBuilder.<SMPP> newBuilder()
          .withStopStrategy(rebindParams.getStopStrategy())
          .withWaitStrategy(rebindParams.getWaitStrategy())
          .retryIfExceptionOfType(rebindParams.getRetryException())
          .build()
          .wrap(sessionBinderFactory.create(smppBean)));
    }

    Runtime.getRuntime().addShutdownHook(new ShutdownHook(smppBeans));
    executor.shutdown();
  }

  /**
   * Gets the receiver sessions. This method delegates to the Utils class to get
   * all bindable sessions
   * 
   * @return the receiver sessions
   */
  private Collection<SMPP> getRxSessions() {
    logger.debug("getRxSessions()");
    return UxoriaUtils.getSessions(BindType.BIND_RX);
  }
}
