/*
 * 
 */
package com.artfully.contrived.smpp.common;

import java.io.IOException;
import java.net.ConnectException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import org.apache.log4j.Logger;
import org.jsmpp.extra.SessionState;
import org.jsmpp.session.SMPPSession;

import com.artfully.contrived.smpp.listeners.IncomingMessageListener;
import com.artfully.contrived.smpp.listeners.SessionStateListener;
import com.artfully.contrived.smpp.model.SMPP;
import com.github.rholder.retry.RetryException;
import com.github.rholder.retry.Retryer;
import com.github.rholder.retry.RetryerBuilder;
import com.google.common.eventbus.EventBus;

public class SessionBinder implements Callable<SMPP>,
		org.jsmpp.session.SessionStateListener {

	private SMPP smpp;
	private final EventBus eventBus;
	private static final Logger logger = Logger.getLogger(SessionBinder.class);
	private final Retryer<SMPP> retryer;

	public SessionBinder(EventBus eventBus, SMPP smpp, RebindParams rebindParams) {
		this.smpp = smpp;
		this.eventBus = eventBus;
		retryer = RetryerBuilder.<SMPP> newBuilder()
				.withStopStrategy(rebindParams.getStopStrategy())
				.withWaitStrategy(rebindParams.getWaitStrategy())
				.retryIfExceptionOfType(rebindParams.getRetryException())
				.build();
	}

	@Override
	public SMPP call() throws Exception {
		SMPPSession session = new SMPPSession();
		String bound = "";
		try {
			bound = session.connectAndBind(smpp.getSMPPServerIP(),
					smpp.getSMPPServerPort(), smpp.getBindParameters());
		} catch (ConnectException NRE) {//
			logger.error("Could not bind will attempt to rebind.");
		} catch (IOException exception) {
			logger.error(exception, exception);
			throw new ConnectException(exception.getMessage());
		}
		if (bound.equals("")) {
			throw new ConnectException("No connection");
		}

		logger.debug("bound--->" + bound);

		smpp.setSession(session);
		eventBus.post(smpp);

		session.setTransactionTimer(5000L);
		session.setMessageReceiverListener(new IncomingMessageListener(eventBus, smpp));
		session.addSessionStateListener(new SessionStateListener(smpp, eventBus));
		session.addSessionStateListener(this);
		session.setEnquireLinkTimer(smpp.getEnquireLinkTimer());

		return smpp;
	}

	@Override
	public void onStateChange(SessionState currentState,
			SessionState originalState, Object obj) {
		logger.debug("onStateChange(). currentState: " + currentState
				+ " originalState: " + originalState + " obj: " + obj);
		eventBus.post(smpp);

		// retry bind here if its not a deliberate close
		if (currentState.equals(SessionState.CLOSED)
				&& !this.smpp.isShuttingDown()) {
			logger.debug("Am supposed to rebind after session closed");
			// TODO improve rebind
			try {
				smpp = this.retryer.call(this);
			} catch (ExecutionException e) {
				logger.error(e, e);
			} catch (RetryException e) {
				logger.error(e, e);
			}
		}
	}
}
