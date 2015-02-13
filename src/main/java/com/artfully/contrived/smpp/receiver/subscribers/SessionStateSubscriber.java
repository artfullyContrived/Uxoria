package com.artfully.contrived.smpp.receiver.subscribers;

import org.apache.log4j.Logger;
import org.jsmpp.session.Session;

import com.artfully.contrived.smpp.model.SMPP;
import com.artfully.contrived.util.UxoriaUtils;
import com.google.common.eventbus.Subscribe;

public class SessionStateSubscriber {

    private static final Logger logger = Logger
	    .getLogger(SessionStateSubscriber.class);

    @Subscribe
    public void updateSessionState(Session session) {
	logger.debug("session " + session.getSessionState());
    }

    @Subscribe
    public void updateSessionState(SMPP bean) {
	logger.debug("bean " + bean);
	UxoriaUtils.updpateSessionState(bean);
    }
}
