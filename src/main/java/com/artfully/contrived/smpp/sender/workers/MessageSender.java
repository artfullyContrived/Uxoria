package com.artfully.contrived.smpp.sender.workers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.Callable;

import org.apache.log4j.Logger;
import org.jsmpp.extra.SessionState;

import com.artfully.contrived.smpp.dtos.ShortMessage;
import com.google.common.util.concurrent.RateLimiter;
import com.inmobia.util.DBUtils;

//TODO remove from message queue if succesfully sent
public class MessageSender implements Callable<String> {

    private static final Logger logger = Logger.getLogger(MessageSender.class);

    private ShortMessage message;
    private final RateLimiter rateLimiter;
    private final Connection conn;
    private final String returnToQueue = "Insert into MessageQueue (id, message, timestamp, recipient, smppid, priority ) "
	    +
	    "values (null,?,?,?,?,?)";

    private String moveToLog = "INSERT INTO MessageLog (id, smppid, type, SMS, receiver, requestConfirmation, timestamp,shortcode ) "
	    + "values(null,?,?,?,?,?,?,?)";

    public MessageSender(ShortMessage message, RateLimiter rateLimiter)
	    throws SQLException {
	this.message = message;
	this.rateLimiter = rateLimiter;
	this.conn = DBUtils.getInstance().getConnection();
    }

    @Override
    public String call() throws Exception {

	String x = null;
	if (!message.getSession().getSessionState().equals(
		SessionState.BOUND_TX)) {
	    return x;
	}
	logger.debug(" Sending message to " + message.getDestinationAddr());

	try {
	    rateLimiter.acquire();
	    x = message.getSession().submitShortMessage(
		    message.getServiceType(),
		    message.getSourceAddrTon(),
		    message.getSourceAddrNpi(),
		    message.getSourceAddr(),
		    message.getDestAddrTon(),
		    message.getDestAddrNpi(),
		    message.getDestinationAddr(),
		    message.getEsmClass(),
		    message.getProtocolId(),
		    message.getPriorityFlag(),
		    message.getScheduleDeliveryTime(),
		    message.getValidityPeriod(),
		    message.getRegisteredDelivery(),
		    message.getReplaceIfPresentFlag(),
		    message.getDataCoding(),
		    message.getSmDefaultMsgId(),
		    message.getShortMessage().getBytes(),// TODO Charset?
		    message.getOptionalParameters());
	    logger.debug("x " + x);
	    // TODO handle resp 88 and resp 20

	    // move to message log
	    PreparedStatement statement = conn.prepareStatement(moveToLog);
	    statement.setInt(1, 2);
	    statement.setString(2, message.getShortMessage());
	    statement.setString(3, "");
	    statement.setString(4, "");
	    statement.setString(5, "");
	    statement.setString(6, "");
	    statement.setString(7, "");
	    statement.execute();
	    conn.close();

	} catch (Exception e) {
	    logger.error(e, e);
	    // return to message queue
	    PreparedStatement statement = conn.prepareStatement(returnToQueue);
	    statement.setString(1, message.getShortMessage());
	    statement.setString(2, message.getScheduleDeliveryTime());
	    statement.setInt(3, 2);// TODO
	    statement.setInt(4, message.getPriorityFlag());
	    statement.execute();
	    conn.close();

	    x = null;
	}
	return x;
    }
}