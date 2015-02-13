package com.artfully.contrived.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;

public final class DBUtils {
	private static final Logger logger = Logger.getLogger(DBUtils.class);
	/*
	 * private final String username = "root"; private final String password =
	 * "";
	 */
	private static DBUtils dbutil;
	private BasicDataSource dataSource;

	static {
		try {
			dbutil = new DBUtils();
		} catch (Exception e) {
			logger.debug("Error connecting to db " + e);
		}
	}

	// we might externalize more of these

	public DBUtils(String propertiesFile) {

		dataSource = new BasicDataSource();
		Properties props = new Properties(
				PropertyUtils.getPropertyFile(propertiesFile));

		dataSource.setDriverClassName(props.getProperty("JDBCSTRING"));
		dataSource.setUsername(props.getProperty("username"));
		dataSource.setPassword(props.getProperty("password"));
		dataSource.setUrl(props.getProperty("CONNSTRING"));
		dataSource.setInitialSize(100);
		dataSource.setMaxActive(99);
		dataSource.setMaxIdle(10);
		dbutil = this;
	}

	public DBUtils(Properties props) {

		dataSource = new BasicDataSource();

		dataSource.setDriverClassName(props.getProperty("JDBCSTRING"));
		dataSource.setUsername(props.getProperty("username"));
		dataSource.setPassword(props.getProperty("password"));
		dataSource.setUrl(props.getProperty("CONNSTRING"));
		dataSource.setInitialSize(100);
		dataSource.setMaxActive(99);
		dataSource.setMaxIdle(10);
		dbutil = this;
	}

	private DBUtils() {

	}

	/**
	 * 
	 * Initializes and returns a database connection to the database
	 * <p>
	 * <code></code>
	 * 
	 * @throws SQLException
	 */
	public synchronized Connection getConnection() throws SQLException {

		return dataSource.getConnection();
	}

	public static DBUtils getInstance() {
		return dbutil;
	}

	public static void closeQuietly(Connection conn) {
		try {
			conn.close();
		} catch (SQLException e) {
			logger.error(e, e);
		}
	}

	public static void closeQuietly(PreparedStatement ps) {
		try {
			ps.close();
		} catch (SQLException e) {
			// quietly
		}
	}

	public DataSource getDatasource() {
		return dataSource;
	}
}