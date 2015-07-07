package com.artfully.contrived.smpp.common;

import java.util.concurrent.Callable;

import org.jsmpp.session.SMPPSession;


//TODO theres a lot of code here that is repeated from Binder. merge the 2?
public class SessionRebinder implements Callable<SMPPSession> {

  @Override
  public SMPPSession call() throws Exception {
    // TODO Auto-generated method stub
    return null;
  }/*

    private SMPP smpp;

    private static final Logger logger = Logger
	    .getLogger(SessionRebinder.class);

    private final Retryer<SMPPSession> retryer;

    private MySessionStateListener sessionStateMonitor;

    private EventBus eventBus;

    public SessionRebinder(SMPP smpp,
	    EventBus eventBus, MySessionStateListener sessionStateMonitor) {
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
	logger.debug("Retry count " + this.retryer);
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

	session.setMessageReceiverListener(new IncomingMessageListener(eventBus,smpp));
	session.addSessionStateListener(sessionStateMonitor);
	session.setEnquireLinkTimer(smpp.getEnquireLinkTimer());

	return session;
    }

    public SMPPSession rebind() throws ExecutionException, RetryException {
	return this.retryer.call(this);
    }
*/}
