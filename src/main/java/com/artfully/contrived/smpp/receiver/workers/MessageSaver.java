/*
 * 
 */
package com.artfully.contrived.smpp.receiver.workers;

import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.jsmpp.bean.DeliverSm;

import com.google.common.math.IntMath;
import com.inmobia.util.DBUtils;

/**
 * The Class Persistor.
 */
public class MessageSaver implements Runnable {

    /** The deliver sm. */
    private final DeliverSm deliverSM;

    private Connection conn;

    /** The Constant logger. */
    private static final Logger logger = Logger.getLogger(MessageSaver.class);

    /**
     * Instantiates a new persistor.
     * 
     * @param take
     *            the take
     */
    public MessageSaver(final DeliverSm deliverSm) {
	this.deliverSM = deliverSm;
	try {
	    this.conn = DBUtils.getInstance().getConnection();
	} catch (SQLException e) {
	    logger.error(e, e);
	}
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    @Override
    // TODO remove the hardcoded values
    // TODO make sure we get th right values
    public void run() {

	logger.debug("Ive been given a message to save: "
		+ Thread.currentThread().getName() + " " + deliverSM);
	String query = "INSERT INTO MessageLog (`ID`,`SMPPID`,`type`,`SMS`,`receiver`,`requestConfirmation`,"
		+
		"`timeStamp`,`messageID`,`delivered`,`deliveryTime`,`sent`,`priority`,`received`,"
		+
		"`status`,`serviceid`,`isCharged`,`number_of_sms`,`price`,`isSubscription`,`shortcode`)"
		+
		" values(null,?,?,?,?,?,now(),?,?,?,?,?,?,?,?,?,?,?,?,?)";
	PreparedStatement ps = null;
	try {
	    ps = conn.prepareStatement(query);

	    ps.setInt(1, 1);// `SMPPID`
	    ps.setInt(2, 2);// `type`
	    ps.setString(3, new String(deliverSM.getShortMessage(), Charset
		    .forName("UTF-8")));// `SMS`
	    ps.setString(4, deliverSM.getSourceAddr());// `receiver`
	    ps.setBoolean(5, deliverSM.isSmeDeliveryAckRequested());// `requestConfirmation`
	    // tmestamp
	    ps.setByte(6, deliverSM.getSmDefaultMsgId());// `messageID`
	    ps.setString(7, "");// `delivered`
	    ps.setString(8, deliverSM.getScheduleDeliveryTime());// `deliveryTime`
	    ps.setBoolean(9, false);// `sent`
	    ps.setByte(10, deliverSM.getPriorityFlag());// `priority`
	    ps.setBoolean(11, true);// `received`
	    ps.setString(12, deliverSM.getCommandStatusAsHex());// `status`
	    ps.setInt(13, 1);// `serviceid`
	    ps.setBoolean(14, false);// `isCharged`
	    ps.setInt(15, IntMath.divide(deliverSM.getShortMessage().length,
		    160, RoundingMode.UP));// `number_of_sms`
	    ps.setString(16, "");// `price`
	    ps.setBoolean(17, false);// `isSubscription`
	    ps.setString(18, deliverSM.getDestAddress());// `shortcode` */

	    ps.executeUpdate();

	} catch (SQLException e) {
	    logger.error(e, e);
	} finally {
	    DBUtils.closeQuietly(ps);
	    DBUtils.closeQuietly(conn);
	}
    }

    public static void main(String[] args) {
	int x = 1601;
	System.out.println(IntMath.divide(x, 160, RoundingMode.UP));
    }
}
