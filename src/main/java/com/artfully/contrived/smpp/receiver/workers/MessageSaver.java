/*
 * 
 */
package com.artfully.contrived.smpp.receiver.workers;

import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.artfully.contrived.smpp.model.MyDeliverSM;
import com.artfully.contrived.util.DBUtils;

/**
 * The Class Persistor.
 */
public class MessageSaver implements Runnable {

  /** The deliver sm. */
  private final MyDeliverSM deliverSM;

  private Connection conn;

  /** The Constant logger. */
  private static final Logger logger = Logger.getLogger(MessageSaver.class);

  private final String query = "INSERT INTO MOMessageLog (`id`,`smppId`,`type`,`message`,`sender`,`requestConfirmation`,`timestamp`,"
      + "`messageID`,`isCharged`,`shortcode`)"
      + " values(null,?,?,?,?,?,now(),?,?,?)";
  /**
   * Instantiates a new persistor.
   * 
   * @param take
   *          the take
   */
  public MessageSaver(final MyDeliverSM deliverSm) {
    this.deliverSM = deliverSm;
    try {
      this.conn = DBUtils.getInstance().getConnection();
    } catch (SQLException e) {
      logger.error(e, e);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Runnable#run()
   */
  @Override
  // TODO get message length
  public void run() {

    logger.debug("incoming message  " + Thread.currentThread().getName() + " "
        + deliverSM);


    PreparedStatement ps = null;
    try {
      ps = conn.prepareStatement(query);

      ps.setInt(1, deliverSM.getID());// `SMPPID`
      ps.setString(2, deliverSM.getEsmClass().name());// `type`
      ps.setString(3, new String(deliverSM.getShortMessage(), Charset.forName("UTF-8")));// `message
      ps.setString(4, deliverSM.getSourceAddr());// `sender`
      ps.setBoolean(5, deliverSM.isSmeDeliveryAckRequested());// `requestConfirmation`
      ps.setInt(6, deliverSM.getSequenceNumber());// `messageID`
     ps.setBoolean(7, false);// `isCharged`
      ps.setString(8, deliverSM.getDestAddress());// `shortcode` 
      ps.executeUpdate();

    } catch (SQLException e) {
      logger.error(e, e);
    } finally {
      DBUtils.closeQuietly(ps);
      DBUtils.closeQuietly(conn);
    }
  }
}
