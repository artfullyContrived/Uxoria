package com.artfully.contrived.smpp.common;

import java.net.ConnectException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.jsmpp.session.SMPPSession;

import com.artfully.contrived.smpp.dtos.SMPP;
import com.artfully.contrived.smpp.listeners.IncomingMessageListener;
import com.artfully.contrived.smpp.listeners.SessionStateListener;
import com.google.common.eventbus.EventBus;
import com.inmobia.utils.retryer.RetryException;
import com.inmobia.utils.retryer.Retryer;
import com.inmobia.utils.retryer.RetryerBuilder;
import com.inmobia.utils.retryer.StopStrategies;
import com.inmobia.utils.retryer.WaitStrategies;

//TODO theres a lot of code here that is repeated from Binder. merge the 2?
public class SessionRebinder implements Callable<SMPPSession> {

    private SMPP smpp;

    private static final Logger logger = Logger
	    .getLogger(SessionRebinder.class);

    private final Retryer<SMPPSession> retryer;

    private SessionStateListener sessionStateMonitor;

    private EventBus eventBus;

    public SessionRebinder(SMPP smpp,
	    EventBus eventBus, SessionStateListener sessionStateMonitor) {
	this.smpp = smpp;
	this.eventBus = eventBus;
	this.sessionStateMonitor = sessionStateMonitor;

	retryer = RetryerBuilder.<SMPPSession> newBuilder()
		.retryIfExceptionOfType(ConnectException.class)
		.withStopStrategy(StopStrategies.stopAfterAttempt(20))
		.withWaitStrategy(
			WaitStrategies.incrementingWait(5, TimeUnit.SECONDS, 5,
				TimeUnit.SECONDS))
		.build();
    }

    @Override
    public SMPPSession call() throws Exception {
	logger.debug("Retry count " + this.retryer.getRetryCount());
	SMPPSession session = new SMPPSession();
	String bound = "";
	try {
	    bound = session.connectAndBind(smpp.getSMPPServerIP(), smpp
		    .getSMPPServerPort(),
		    smpp.getBindParameters());
	} catch (ConnectException NRE) {//
	    logger.error("Could not bind will attempt to rebind.");
	}
	if (bound.equals("")) {
	    throw new ConnectException();
	}

	logger.debug("bound--->" + bound);

	smpp.setSession(session);
	eventBus.post(smpp);

	session.setMessageReceiverListener(new IncomingMessageListener(eventBus));
	session.addSessionStateListener(sessionStateMonitor);
	session.setEnquireLinkTimer(smpp.getEnquireLinkTimer());

	return session;
    }

    public SMPPSession rebind() throws ExecutionException, RetryException {
	return this.retryer.call(this);
    }
}
