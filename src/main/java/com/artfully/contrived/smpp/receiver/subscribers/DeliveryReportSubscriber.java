/*
 * 
 */
package com.artfully.contrived.smpp.receiver.subscribers;

import org.apache.log4j.Logger;
import org.jsmpp.bean.DeliveryReceipt;

import com.google.common.eventbus.Subscribe;

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

    /**
     * Update smpp receiver.
     * 
     * @param deliveryReceipt
     *            the delivery receipt
     */
    @Subscribe
    // TODO Make it work
    public void updateSMPPReceiver(DeliveryReceipt deliveryReceipt) {
	logger.debug("updateSMPPReceiver(). deliveryReceipt: "
		+ deliveryReceipt);
	long id = Long.parseLong(deliveryReceipt.getId()) & 0xffffffff;
	String messageId = Long.toString(id, 16).toUpperCase();
	logger.debug(" " + messageId);

    }
}
