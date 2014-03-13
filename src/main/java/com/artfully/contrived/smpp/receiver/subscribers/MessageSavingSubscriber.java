/*
 * 
 */
package com.artfully.contrived.smpp.receiver.subscribers;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.log4j.Logger;
import org.jsmpp.bean.DeliverSm;

import com.artfully.contrived.smpp.receiver.workers.MessageSaver;
import com.google.common.eventbus.Subscribe;
import com.google.common.util.concurrent.MoreExecutors;

/**
 * Called on receipt of a new message.
 * This class is responsible for persisting the incoming message to DB.
 * THE saveToDb method is called for every incoming message
 */
public class MessageSavingSubscriber {

    private static final Logger logger = Logger
	    .getLogger(MessageSavingSubscriber.class);

    // TODO Its not safe to use a cached thread pool?
    private static final ExecutorService service = MoreExecutors
	    .getExitingExecutorService((ThreadPoolExecutor) Executors
		    .newCachedThreadPool());

    @Subscribe
    public void saveToDB(DeliverSm deliverSm) throws InterruptedException {

	logger.debug("DB SUBSCRIBER HAS BEEN CALLED: "
		+ new String(deliverSm.getShortMessage()));

	service.submit(new MessageSaver(deliverSm));
    }

}
