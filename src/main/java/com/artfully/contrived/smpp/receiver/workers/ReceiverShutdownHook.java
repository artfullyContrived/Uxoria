/*
 * 
 */
package com.artfully.contrived.smpp.receiver.workers;

import java.util.Collection;

import com.artfully.contrived.smpp.model.SMPP;
import com.artfully.contrived.util.UxoriaUtils;

public class ReceiverShutdownHook extends Thread {

    private Collection<SMPP> sessions;

  public ReceiverShutdownHook(Collection<SMPP> smppBeans) {
    this.sessions = smppBeans;
    }

    @Override
    public void run() {
      System.err.println("we have to shutdown!!!!");

	for (SMPP smpp : sessions) {
	    System.out.println("SHUTTING DOWN " + smpp.getSystemID());
	    smpp.setShuttingDown(true);
	    UxoriaUtils.updateSessionState(smpp);
	    smpp.getSession().unbindAndClose();
	}
    }
}
