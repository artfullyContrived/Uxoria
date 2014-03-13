/*
 * 
 */
package com.artfully.contrived.smpp.receiver.workers;

import java.util.Collection;

import com.artfully.contrived.smpp.dtos.SMPP;

public class ReceiverShutdownHook extends Thread {

    private Collection<SMPP> sessions;

    public ReceiverShutdownHook(Collection<SMPP> smppBeans) {
	this.sessions = smppBeans;
    }

    @Override
    public void run() {

	for (SMPP smpp : sessions) {
	    System.out.println("SHUTTING DOWN " + smpp.getSystemID());
	    smpp.setShuttingDown(true);
	    smpp.getSession().unbindAndClose();
	}
    }
}
