/*
 * 
 */
package com.artfully.contrived.util;

import java.util.Collection;

import com.artfully.contrived.smpp.model.SMPP;

public class ShutdownHook extends Thread {

    private Collection<SMPP> sessions;

  public ShutdownHook(Collection<SMPP> smppBeans) {
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
