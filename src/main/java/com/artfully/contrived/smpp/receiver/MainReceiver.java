/*
 * 
 */
package com.artfully.contrived.smpp.receiver;

import java.io.IOException;
import java.util.Collection;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.jsmpp.bean.BindType;

import com.artfully.contrived.smpp.common.RebindParams;
import com.artfully.contrived.smpp.common.SessionBinder;
import com.artfully.contrived.smpp.dtos.SMPP;
import com.artfully.contrived.smpp.receiver.subscribers.ContentHandlingSubscriber;
import com.artfully.contrived.smpp.receiver.subscribers.DeliveryReportSubscriber;
import com.artfully.contrived.smpp.receiver.subscribers.MessageSavingSubscriber;
import com.artfully.contrived.smpp.receiver.subscribers.SessionStateSubscriber;
import com.artfully.contrived.smpp.receiver.workers.ReceiverShutdownHook;
import com.artfully.contrived.util.Props;
import com.artfully.contrived.util.UxoriaUtils;
import com.google.common.eventbus.EventBus;
import com.inmobia.util.PropertyUtils;
import com.inmobia.utils.retryer.Retryer;

/**
 * The MainReceiver is the starting point for the receiver service.
 */
// TODO improve the naming of the classes to a way that makes sense
// TODO take care of CharEncoding everywhere
public class MainReceiver {

    /** The props. */
    private final Properties props;

    /** The Constant logger. */
    private static final Logger logger = Logger.getLogger(MainReceiver.class);

    /** The Constant eventBus. */
    private static EventBus eventBus = new EventBus();

    /**
     * Instantiates a new receiver.
     */
    private MainReceiver() {
	Properties log4jprops = PropertyUtils
		.getPropertyFile(Props.log4jPropertyFile.getFileName());
	PropertyConfigurator.configure(log4jprops);

	props = PropertyUtils.getPropertyFile(Props.receiverPropertyFile
		.getFileName());
	// TODO this shouldn't be here
	// http://misko.hevery.com/code-reviewers-guide/flaw-constructor-does-real-work/
	UxoriaUtils.initialize(props);

    }

    /**
     * The main method.
     * 
     * @param args
     *            the arguments
     * @throws IOException
     */

    public static void main(String[] args) throws IOException {

	MainReceiver receiver = new MainReceiver();

	eventBus.register(new ContentHandlingSubscriber());
	eventBus.register(new MessageSavingSubscriber());
	eventBus.register(new DeliveryReportSubscriber());
	eventBus.register(new SessionStateSubscriber());

	Collection<SMPP> smppBeans = receiver.getRxSessions();
	ExecutorService executor = Executors.newCachedThreadPool();
	RebindParams rebindParams = new RebindParams(receiver.props);

	// get bindable sessions and pass them to binder
	try {
	    Future<SMPP> future = null;
	    for (SMPP smppBean : smppBeans) {
		future = executor.submit(new Retryer<SMPP>(
			rebindParams.getStopStrategy(),
			rebindParams.getWaitStrategy(),
			rebindParams.getRejectPredicate())
			.wrap(new SessionBinder(eventBus, smppBean,
				rebindParams)));
	    }
	    SMPP smpp;

	    smpp = future.get();
	    logger.debug("SESSION: " + smpp.getSession().getSessionId());
	} catch (InterruptedException e) {
	    logger.error(e, e);
	} catch (ExecutionException e) {
	    logger.error(e, e);
	}

	Runtime.getRuntime().addShutdownHook(
		new ReceiverShutdownHook(smppBeans));
	executor.shutdown();

    }

    /**
     * Gets the receiver sessions.
     * This method delegates to the Utils class to get all bindable sessions
     * 
     * @return the receiver sessions
     */
    private Collection<SMPP> getRxSessions() {
	logger.debug("getRxSessions()");
	return UxoriaUtils.getSessions(BindType.BIND_RX);
    }
}
