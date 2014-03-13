/*
 * 
 */
package com.artfully.contrived.smpp.listeners;

import org.apache.log4j.Logger;
import org.jsmpp.extra.SessionState;

import com.artfully.contrived.smpp.dtos.SMPP;
import com.google.common.eventbus.EventBus;

/**
 * Gets Called on Session state changed.
 * Responsible for Updating SMPP table with bind status. And any other necessary
 * duties
 */
public class SessionStateListener implements
	org.jsmpp.session.SessionStateListener {

    private static final Logger logger = Logger
	    .getLogger(SessionStateListener.class);
    private final SMPP smppBean;
    private EventBus eventBus;

    public SessionStateListener(SMPP smppBean, EventBus eventBus) {
	this.smppBean = smppBean;
	this.eventBus = eventBus;
    }

    @Override
    public void onStateChange(SessionState currentState,
	    SessionState originalState, Object obj) {

	logger.debug(" StateChanged() Old State---> " + originalState
		+ " current state ---> "
		+ currentState);
	eventBus.post(smppBean);

	// retry bind here if its not a deliberate close
	// Cmmented out with a view to deleting
	/*
	 * if (currentState.equals(SessionState.CLOSED)
	 * && !this.smppBean.isShuttingDown()) {
	 * logger.debug("Am supposed to rebind ");
	 * try {
	 * new SessionRebinder(smppBean, eventBus, this).rebind();
	 * } catch (ExecutionException e) {
	 * logger.error(e, e);
	 * } catch (RetryException e) {
	 * logger.error(e, e);
	 * }
	 * }
	 */
    }
}
