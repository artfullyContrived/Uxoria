package com.artfully.contrived.smpp.sender;


import java.util.Collection;
import java.util.LinkedList;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.jsmpp.bean.BindType;

import com.artfully.contrived.smpp.common.RebindParams;
import com.artfully.contrived.smpp.common.SessionBinder;
import com.artfully.contrived.smpp.dtos.SMPP;
import com.artfully.contrived.smpp.receiver.subscribers.DeliveryReportSubscriber;
import com.artfully.contrived.smpp.receiver.subscribers.SessionStateSubscriber;
import com.artfully.contrived.smpp.receiver.workers.ReceiverShutdownHook;
import com.artfully.contrived.smpp.sender.workers.MessageQueuePoller;
import com.artfully.contrived.util.Props;
import com.artfully.contrived.util.UxoriaUtils;
import com.google.common.eventbus.EventBus;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.Service;
import com.inmobia.util.PropertyUtils;
import com.inmobia.utils.retryer.Retryer;

public class MainSender implements Service {

    /** The props. */
    private final Properties props;

    /** The Constant logger. */
    private static final Logger logger = Logger.getLogger(MainSender.class);

    /** The Constant eventBus. */
    private static EventBus eventBus;

    /**
     * Instantiates a new receiver.
     */
    private MainSender() {

	Properties log4jprops = PropertyUtils
		.getPropertyFile(Props.log4jPropertyFile.getFileName());
	PropertyConfigurator.configure(log4jprops);
	eventBus = new EventBus();
	props = PropertyUtils.getPropertyFile(Props.receiverPropertyFile
		.getFileName());
	UxoriaUtils.initialize(props);

    }

    public static void main(String[] args) {
	MainSender sender = new MainSender();
	Collection<SMPP> smppBeans = sender.getTxSessions();

	ExecutorService bindExecutor = Executors.newCachedThreadPool();
	ExecutorService consumerService = Executors.newFixedThreadPool(1000);
	ScheduledExecutorService producerService = Executors
		.newScheduledThreadPool(5);
	RebindParams rebindParams = new RebindParams(sender.props);

	Collection<Future<SMPP>> futures = new LinkedList<Future<SMPP>>();

	eventBus.register(new DeliveryReportSubscriber());
	eventBus.register(new SessionStateSubscriber());

	// get bindable sessions and pass them to binder
	try {
	    Future<SMPP> future = null;
	    for (SMPP smppBean : smppBeans) {
		future = bindExecutor.submit(new Retryer<SMPP>(
			rebindParams.getStopStrategy(),
			rebindParams.getWaitStrategy(),
			rebindParams.getRejectPredicate())
			.wrap(new SessionBinder(eventBus, smppBean, rebindParams)));

		futures.add(future);
	    }

	    SMPP smpp = null;
	    for (Future<SMPP> session : futures) {
		smpp = session.get();

		producerService.scheduleWithFixedDelay(
			new MessageQueuePoller(smpp, consumerService), 0,
			10, TimeUnit.SECONDS);

	    }
	} catch (InterruptedException e) {
	    logger.error(e, e);
	} catch (ExecutionException e) {
	    logger.error(e, e);
	}

	Runtime.getRuntime().addShutdownHook(new ReceiverShutdownHook(smppBeans));
	bindExecutor.shutdown();
    }

    private Collection<SMPP> getTxSessions() {
	logger.debug("getTxSessions().");
	return UxoriaUtils.getSessions(BindType.BIND_TX);
    }

    @Override
    public void addListener(Listener arg0, Executor arg1) {
	// TODO Auto-generated method stub
	
    }

    @Override
    public Throwable failureCause() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public boolean isRunning() {
	// TODO Auto-generated method stub
	return false;
    }

    @Override
    public ListenableFuture<State> start() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public State startAndWait() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public State state() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public ListenableFuture<State> stop() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public State stopAndWait() {
	// TODO Auto-generated method stub
	return null;
    }
}
