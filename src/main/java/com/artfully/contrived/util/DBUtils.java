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

  private static DBUtils dbutil;
  private BasicDataSource dataSource;

  static {
    try {
      dbutil = new DBUtils();
    } catch (Exception e) {
      logger.debug("Error connecting to db " + e);
    }
  }



  public DBUtils(String propertiesFile) {

    dataSource = new BasicDataSource();

    Properties props = new Properties(PropertyUtils.getPropertiesFromFile(propertiesFile));
    // we might externalize more of these
    dataSource.setDriverClassName(props.getProperty("JDBCSTRING"));
    dataSource.setUsername(props.getProperty("username"));
    dataSource.setPassword(props.getProperty("password"));
    dataSource.setUrl(props.getProperty("CONNSTRING"));
    dataSource.setRemoveAbandonedTimeout(30);
    dataSource.setRemoveAbandoned(true);
    dataSource.setInitialSize(100);
    dataSource.setMaxActive(99);
    dataSource.setMaxIdle(1);
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
  }

  private DBUtils() {

  }

  /**
   * 
   * Initializes and returns a database connection
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
    if (conn != null) {
      try {
        conn.close();
      } catch (SQLException e) {
        // quietly
      }
    }
  }

  public static void closeQuietly(PreparedStatement ps) {
    if (ps != null) {
      try {
        ps.close();
      } catch (SQLException e) {
        // quietly
      }
    }
  }

  public DataSource getDatasource() {
    return dataSource;
  }
}
