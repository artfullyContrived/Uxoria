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
 * 
 * This class is responsible for updating the Receiver log with the status and actioning as
 * appropriate.
 */

public class DeliveryReportSubscriber {

  private static final Logger logger = Logger.getLogger(DeliveryReportSubscriber.class);

  private static final ExecutorService service = MoreExecutors
      .getExitingExecutorService((ThreadPoolExecutor) Executors.newFixedThreadPool(10));

  /**
   * Update smpp receiver.
   * 
   * @param deliveryReceipt the delivery receipt
   */
  @Subscribe
  public void updateSMPPReceiver(DeliveryReceipt deliveryReceipt) {
    logger.debug("updateSMPPReceiver(). deliveryReceipt: " + deliveryReceipt);
    long id = Long.parseLong(deliveryReceipt.getId()) & 0xffffffffL;
    String messageId = Long.toString(id, 16).toUpperCase();
    logger.debug(" " + messageId);

    service.submit(new MTMessageLogUpdater(deliveryReceipt));
  }
}
