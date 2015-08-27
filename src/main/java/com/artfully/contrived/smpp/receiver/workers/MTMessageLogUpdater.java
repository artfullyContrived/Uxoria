/*
 * 
 */
package com.artfully.contrived.smpp.receiver.workers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.jsmpp.bean.DeliveryReceipt;

import com.artfully.contrived.util.DBUtils;

/**
 * The Class Persistor.
 */
public class MTMessageLogUpdater implements Runnable {

  /** The deliver sm. */
  private final DeliveryReceipt deliverSM;

  private Connection conn;

  /** The Constant logger. */
  private static final Logger logger = Logger.getLogger(MTMessageLogUpdater.class);

  /**
   * Instantiates a new persistor.
   * 
   * @param take the take
   */
  public MTMessageLogUpdater(final DeliveryReceipt deliveryReceipt) {
    this.deliverSM = deliveryReceipt;
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
  public void run() {

    logger.debug("Ive been given a message to save: " + Thread.currentThread().getName() + " "
        + deliverSM);
    String query = "UPDATE MTMessageLog  set delivered = ? where messageId =?";
    PreparedStatement ps = null;
    try {
      ps = conn.prepareStatement(query);

      ps.setInt(1, deliverSM.getDelivered());// `SMPPID`
      ps.setString(2, deliverSM.getId());// `type`

      ps.executeUpdate();

    } catch (SQLException e) {
      logger.error(e, e);
    } finally {
      DBUtils.closeQuietly(conn);
    }
  }
}
