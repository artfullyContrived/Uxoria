/*
 * 
 */
package com.artfully.contrived.smpp.receiver.subscribers;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.log4j.Logger;

import com.artfully.contrived.smpp.model.MyDeliverSM;
import com.artfully.contrived.smpp.receiver.workers.HTTPContentHandler;
import com.google.common.eventbus.Subscribe;
import com.google.common.util.concurrent.MoreExecutors;

/**
 * Get's called when a new message comes in. This class is responsible for getting the contentURL
 * and hitting it. What do we do with the content we get?
 */
public class ContentHandlingSubscriber {

  private static final Logger logger = Logger.getLogger(ContentHandlingSubscriber.class);

  private static final ExecutorService service = MoreExecutors
      .getExitingExecutorService((ThreadPoolExecutor) Executors.newFixedThreadPool(10));

  @Subscribe
  public void getContent(MyDeliverSM deliverSm) {
    System.err.println("getContent(). for sms : " + new String(deliverSm.getShortMessage()));
    logger.debug("getContent(). for sms : " + new String(deliverSm.getShortMessage()));

    service.submit(new HTTPContentHandler(deliverSm));

  }
}
