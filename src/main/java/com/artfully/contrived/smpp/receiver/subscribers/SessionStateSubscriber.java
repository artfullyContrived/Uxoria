package com.artfully.contrived.smpp.receiver.subscribers;

import org.apache.log4j.Logger;

import com.artfully.contrived.smpp.model.SMPP;
import com.artfully.contrived.util.UxoriaUtils;
import com.google.common.eventbus.Subscribe;

// TODO metric number of session state changes
public class SessionStateSubscriber {

  private static final Logger logger = Logger.getLogger(SessionStateSubscriber.class);

  @Subscribe
  public void updateSessionState(SMPP smpp) {
    logger.debug("bean " + smpp);

    UxoriaUtils.updateSessionState(smpp);
  }
}
