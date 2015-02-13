package com.artfully.contrived.smpp.sender.workers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.concurrent.ExecutorService;

import org.apache.log4j.Logger;
import org.jsmpp.bean.Alphabet;
import org.jsmpp.bean.GeneralDataCoding;
import org.jsmpp.bean.NumberingPlanIndicator;
import org.jsmpp.bean.OptionalParameters;
import org.jsmpp.bean.RegisteredDelivery;
import org.jsmpp.bean.SMSCDeliveryReceipt;
import org.jsmpp.bean.TypeOfNumber;
import org.jsmpp.extra.SessionState;
import org.jsmpp.util.RelativeTimeFormatter;
import org.jsmpp.util.TimeFormatter;

import com.artfully.contrived.smpp.model.MessageQueue;
import com.artfully.contrived.smpp.model.SMPP;
import com.artfully.contrived.smpp.model.ShortMessage;
import com.artfully.contrived.util.DBUtils;
import com.google.common.util.concurrent.RateLimiter;

//TODO only get messages for bound smpp

/**
 * SMPPTOSend poller.
 * <p>
 * This class polls the SMPPTosend table every so often for messages for is
 * smppid. It then puts them in a priority queue where consumers pick from for
 * onward sending
 */
public class MessageQueuePoller implements Runnable {

	private static final Logger logger = Logger
			.getLogger(MessageQueuePoller.class);
	private static final TimeFormatter TIME_FORMATTER = new RelativeTimeFormatter();
	private final SMPP smpp;
	private final RateLimiter limiter;

	private ExecutorService consumerService;

	public MessageQueuePoller(SMPP smppBean, ExecutorService consumerService) {
		this.smpp = smppBean;
		this.consumerService = consumerService;
		this.limiter = RateLimiter.create(smpp.getTPS());
	}

	@Override
	public void run() {
		logger.debug("polling for " + smpp.getID());
		if (!smpp.getSession().getSessionState().equals(SessionState.BOUND_TX)) {
			logger.debug("Not bound we dont add to queue");
			return;
		}

		String query = "SELECT * FROM MessageQueue WHERE smppId= "
				+ smpp.getID() + " LIMIT 10";
		Connection con = null;
		try {
			con = DBUtils.getInstance().getConnection();
			PreparedStatement preparedStatement = con.prepareStatement(query);
			ResultSet resultSet = preparedStatement.executeQuery();
			ShortMessage message;
			MessageQueue messageQueue;
			while (resultSet.next()) {
				messageQueue = new MessageQueue(resultSet);
				message = new ShortMessage.Builder(smpp.getSession())
						.dataCoding(
								new GeneralDataCoding(Alphabet
										.valueOf((byte) smpp.getDataEncoding())))
						.destAddrNpi(
								NumberingPlanIndicator.valueOf(smpp.getNPI()))
						.destAddrTon(TypeOfNumber.valueOf(smpp.getTON()))
						.destinationAddr(resultSet.getString("recipient"))
						.priorityFlag(resultSet.getByte("priority"))
						.optionalParameters(
								OptionalParameters.newSarTotalSegments(2))
						.shortMessage(resultSet.getString("message"))
						.sourceAddr(smpp.getShortCode())
						.sourceAddrNpi(
								NumberingPlanIndicator.valueOf(smpp.getNPI()))
						.sourceAddrTon(TypeOfNumber.valueOf(smpp.getTON()))
						// TODO get a way to say if u want a delivery report in
						// mesagequeue
						.registeredDelivery(
								new RegisteredDelivery(
										SMSCDeliveryReceipt.SUCCESS_FAILURE))
						.scheduleDeliveryTime(
								TIME_FORMATTER.format(resultSet
										.getDate("timestamp")))
						.validityPeriod(TIME_FORMATTER.format(new Date()))
						.build();
				logger.debug("Added new message " + message);
				// TODO pass MessageQueue instead of SMPP
				consumerService
						.submit(new MessageSender(message, messageQueue, limiter));

			}

		} catch (SQLException e) {
			logger.error(e, e);
		} finally {
			DBUtils.closeQuietly(con);
		}
	}
}