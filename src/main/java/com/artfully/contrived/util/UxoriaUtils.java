package com.artfully.contrived.util;

import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.log4j.Logger;
import org.jsmpp.bean.BindType;

import com.artfully.contrived.smpp.model.ContentItem;
import com.artfully.contrived.smpp.model.MyDeliverSM;
import com.artfully.contrived.smpp.model.SMPP;

/**
 * The Class UxoriaUtils.
 */
public class UxoriaUtils {

	/** The db utils. */
	private static DBUtils dbUtils;

	private static Properties props;

	/** The logger. */
	private static Logger logger = Logger.getLogger(UxoriaUtils.class);

	/** The skey spec. */
	static SecretKeySpec skeySpec = null;

	private UxoriaUtils() {

	}

	public static void initialize(Properties properties) {
		props = properties;
		dbUtils = new DBUtils(properties);
	}

	/**
	 * AES encrypt.
	 * 
	 * @param decrypted
	 *            the decrypted
	 * @return the byte[]
	 */
	public static byte[] AESEncrypt(String decrypted) {

		Cipher cipher;
		byte[] encrypted = null;
		try {
			cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, getSkeySpec());
			encrypted = cipher.doFinal(decrypted.getBytes());
		} catch (NoSuchAlgorithmException e) {
			logger.error(e, e);
		} catch (NoSuchPaddingException e) {
			logger.error(e, e);
		} catch (InvalidKeyException e) {
			logger.error(e, e);
		} catch (IllegalBlockSizeException e) {
			logger.error(e, e);
		} catch (BadPaddingException e) {
			logger.error(e, e);
		}

		return encrypted;

	}

	/**
	 * AES decrypt.
	 * 
	 * @param encrypted
	 *            the encrypted
	 * @return the byte[]
	 */
	public static byte[] AESDecrypt(byte[] encrypted) {
		Cipher cipher;
		byte[] original = null;
		try {
			cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, getSkeySpec());
			original = cipher.doFinal(encrypted);
		} catch (NoSuchAlgorithmException e) {
			logger.error(e, e);
		} catch (NoSuchPaddingException e) {
			logger.error(e, e);
		} catch (InvalidKeyException e) {
			logger.error(e, e);
		} catch (IllegalBlockSizeException e) {
			logger.error(e, e);
		} catch (BadPaddingException e) {
			logger.error(e, e);
		}

		return original;
	}

	/**
	 * Gets the skey spec.
	 * 
	 * @return the skey spec
	 */
	private static Key getSkeySpec() {
		if (skeySpec != null) {
			return skeySpec;
		}
		KeyGenerator kgen;
		SecretKey skey;
		byte[] raw;

		try {
			kgen = KeyGenerator.getInstance("AES");
			// kgen.init(128);
			skey = kgen.generateKey();
			raw = skey.getEncoded();
			skeySpec = new SecretKeySpec(raw, "AES");
		} catch (NoSuchAlgorithmException e) {
			logger.error(e, e);
		}
		return skeySpec;

	}

	public static List<SMPP> getSessions(BindType bindType) {

		logger.debug("bind type " + bindType.name());
		List<SMPP> sessions = new LinkedList<SMPP>();
		String property = props.getProperty("limit");

		logger.debug("limit---> " + property);
		// in case limit is specified
		String limit = property == null ? "" : " and " + property;
		String sql = "select * from SMPP where IsEnabled=1 and bindType=?" + limit
				+ " order  by id;";
		Connection conn = null;
		try {
			conn = dbUtils.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, bindType.name());
			ResultSet resultSet = ps.executeQuery();
			SMPP smpp;
			System.out.println(sql + " " + bindType.name());
			while (resultSet.next()) {
				smpp = new SMPP();
				smpp.setBindType(resultSet.getString("bindType"));
				smpp.setDataEncoding(resultSet.getByte("dataEncoding"));
				smpp.setEnquireLinkTimer(resultSet.getInt("enquireLinkTimer"));
				smpp.setID(resultSet.getInt("Id"));
				smpp.setNPI(resultSet.getByte("NPI"));
				smpp.setPassword(resultSet.getString("password"));
				smpp.setShortCode(resultSet.getString("shortCode"));
				smpp.setSMPPServerIP(resultSet.getString("SMPPServerIP"));
				smpp.setSMPPServerPort(resultSet.getInt("SMPPServerPort"));
				smpp.setSystemID(resultSet.getString("systemId"));
				smpp.setSystemType(resultSet.getString("systemType"));
				smpp.setTON(resultSet.getByte("TON"));
				smpp.setTPS(resultSet.getInt("tps"));
				sessions.add(smpp);

			}

			logger.debug("SESSIONS " + sessions);
		} catch (SQLException e) {
			logger.error(e, e);
		} finally {
			DBUtils.closeQuietly(conn);
		}
		return sessions;
	}

	public static void updateSessionState(SMPP smpp) {
		logger.debug("updpateSessionState(). bean: " + smpp);
		Connection conn = null;
		try {
			conn = dbUtils.getConnection();
			String query = "Update SMPP set status=? where id=?";
			PreparedStatement preparedStatement;

			preparedStatement = conn.prepareStatement(query);
			preparedStatement.setString(1, smpp.getSession().getSessionState()
					.name());
			System.out.println("sasas "
					+ smpp.getSession().getSessionState().name());
			preparedStatement.setInt(2, smpp.getID());
			int x = preparedStatement.executeUpdate();
			logger.debug("Updated " + x + " sessions");
			preparedStatement.close();

		} catch (SQLException e) {
			logger.error(e, e);
		} finally {
			DBUtils.closeQuietly(conn);
		}
	}

	public static ContentItem getContentElement(MyDeliverSM deliverSm2) {
		logger.debug("getContentElement(). deliverSm: "
				+ new String(deliverSm2.getShortMessage()));
		String query = "SELECT Id, keyword,headText, contentURL,tailText from ContentItem where shortcode=? and (keyword=? or keyword='default')order by if(keyword='default',1,0) ";
		ContentItem contentElement = null;
		String message = new String(deliverSm2.getShortMessage(),
				Charset.forName("UTF-8"));

		String keyword = message.indexOf(' ') > 0 ? message.substring(0,
				message.indexOf(' ')) : message;
		Connection conn = null;
		try {
			conn = dbUtils.getConnection();
			PreparedStatement preparedStatement = conn.prepareStatement(query);
			preparedStatement.setString(1, deliverSm2.getDestAddress());
			preparedStatement.setString(2, keyword);
			ResultSet rs = preparedStatement.executeQuery();

			if (rs.next()) {
				contentElement = new ContentItem();
				contentElement.setId(rs.getInt("Id"));
				contentElement.setHeadText(rs.getString("headText"));
				contentElement.setContentURL(rs.getString("contentURL"));
				contentElement.setTailText(rs.getString("tailText"));
				contentElement.setKeyword(rs.getString("keyword"));
			}
		} catch (SQLException e) {
			logger.error(e, e);
		} finally {
			DBUtils.closeQuietly(conn);
		}
		logger.debug("Content Element " + contentElement);
		return contentElement;
	}

	public static void pushToMessageQueue(String message, String recipient,
			String shortcode) {
		logger.debug("pushToMessageQueue()." + message + " " + recipient + " "
				+ shortcode);
		String sql = "INSERT INTO MessageQueue (id,message, timestamp, recipient,smppid,priority) values(null,?,now(),?,(select id from SMPP where bindType='BIND_TX' and shortcode=? limit 1),?)";
		Connection connection = null;
		try {
			connection = dbUtils.getConnection();
			PreparedStatement preparedStatement = connection
					.prepareStatement(sql);
			preparedStatement.setString(1, message);
			preparedStatement.setString(2, recipient);
			preparedStatement.setString(3, shortcode);
			preparedStatement.setInt(4, 0);
			preparedStatement.executeUpdate();
			connection.close();
		} catch (SQLException e) {
			logger.error(e, e);
		} finally {
			DBUtils.closeQuietly(connection);
		}
	}
}
