/*
 * 
 */
package com.artfully.contrived.smpp.receiver.subscribers;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.log4j.Logger;
import org.jsmpp.bean.DeliveryReceipt;

import com.artfully.contrived.smpp.receiver.workers.MTMessageLogUpdater;
import com.google.common.eventbus.Subscribe;
import com.google.common.util.concurrent.MoreExecutors;

// TODO: Auto-generated Javadoc
/**
 * Gets called when the incoming message is a delivery report.
 * This class is responsible for updating the Receiver log with the status. And
 * actioning as appropriate.
 */

public class DeliveryReportSubscriber {

    /** The Constant logger. */
    private static final Logger logger = Logger
	    .getLogger(DeliveryReportSubscriber.class);
    
    // TODO Its not safe to use a cached thread pool?
    private static final ExecutorService service = MoreExecutors
	    .getExitingExecutorService((ThreadPoolExecutor) Executors
		    .newCachedThreadPool());

    /**
     * Update smpp receiver.
     * 
     * @param deliveryReceipt
     *            the delivery receipt
     */
    @Subscribe
    // TODO Also call back a billing class for the SMSService
    public void updateSMPPReceiver(DeliveryReceipt deliveryReceipt) {
	logger.debug("updateSMPPReceiver(). deliveryReceipt: "
		+ deliveryReceipt);
	long id = Long.parseLong(deliveryReceipt.getId()) & 0xffffffff;
	String messageId = Long.toString(id, 16).toUpperCase();
	logger.debug(" " + messageId);
	
	service.submit(new MTMessageLogUpdater(deliveryReceipt));

    }
}
