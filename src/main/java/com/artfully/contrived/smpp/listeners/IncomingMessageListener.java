/*
 * 
 */
package com.artfully.contrived.smpp.listeners;

import java.nio.charset.Charset;

import org.apache.log4j.Logger;
import org.jsmpp.bean.AlertNotification;
import org.jsmpp.bean.DataSm;
import org.jsmpp.bean.DeliverSm;
import org.jsmpp.bean.DeliveryReceipt;
import org.jsmpp.bean.MessageType;
import org.jsmpp.extra.ProcessRequestException;
import org.jsmpp.session.DataSmResult;
import org.jsmpp.session.MessageReceiverListener;
import org.jsmpp.session.Session;
import org.jsmpp.util.InvalidDeliveryReceiptException;

import com.google.common.base.Preconditions;
import com.google.common.eventbus.EventBus;

// TODO: Auto-generated Javadoc
/**
 * This is a callback for incoming messages.
 * New Messages are received here then the subscribers informed
 * We then proceed to log the message and Convert to HTTPRequest
 */
public class IncomingMessageListener implements MessageReceiverListener {

    /** The Constant logger. */
    private static final Logger logger = Logger
	    .getLogger(IncomingMessageListener.class);
    private final EventBus eventBus;

    public IncomingMessageListener(EventBus eventBus) {
	Preconditions.checkNotNull(eventBus, "Event Bus must not be null");
	this.eventBus = eventBus;
    }

    /*
     * (non-Javadoc)
     * @see org.jsmpp.session.GenericMessageReceiverListener#onAcceptDataSm(org.jsmpp.bean.DataSm, org.jsmpp.session.Session)
     */
    @Override
    public DataSmResult onAcceptDataSm(DataSm dataSM, Session session)
	    throws ProcessRequestException {
	logger.debug("in onAcceptDataSm()");

	return null;
    }

    /*
     * (non-Javadoc)
     * @see org.jsmpp.session.MessageReceiverListener#onAcceptAlertNotification(org.jsmpp.bean.AlertNotification)
     */
    @Override
    public void onAcceptAlertNotification(AlertNotification alertNotification) {
	logger.debug("in onAcceptAlertNotification()");
	logger.debug("onAceptAlertNotification" + alertNotification);

    }

    /*
     * (non-Javadoc)
     * @see org.jsmpp.session.MessageReceiverListener#onAcceptDeliverSm(org.jsmpp.bean.DeliverSm)
     */
    @Override
    public void onAcceptDeliverSm(DeliverSm deliverSm)
	    throws ProcessRequestException {
	// Its a delivery report
	if (MessageType.SMSC_DEL_RECEIPT.containedIn(deliverSm.getEsmClass())) {
	    try {
		logger.debug(deliverSm.getShortMessageAsDeliveryReceipt());
		DeliveryReceipt delReceipt = deliverSm
			.getShortMessageAsDeliveryReceipt();
		logger.debug("delivery report " + delReceipt);
		eventBus.post(deliverSm.getShortMessageAsDeliveryReceipt());

	    } catch (InvalidDeliveryReceiptException e) {
		logger.error(e, e);
	    }
	} else {// its a new message
	    logger.debug("new SMS : "
		    + new String(deliverSm.getShortMessage(), Charset
			    .forName("UTF-8")));
	    eventBus.post(deliverSm);

	}
    }
}
