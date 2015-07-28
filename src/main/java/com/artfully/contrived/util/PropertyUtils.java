package com.artfully.contrived.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import org.apache.log4j.Logger;

public class PropertyUtils {

  private static final Logger logger = Logger.getLogger(PropertyUtils.class);
  private Properties properties;

  public PropertyUtils(String filename) {
    properties = getPropertiesFromFile(filename);
  }

  public static Properties getPropertiesFromFile(String filename) {

    Properties prop = new Properties();
    InputStream inputStream = null;

    String path;
    try {
      path = System.getProperty("user.dir") + System.getProperty("file.separator") + filename;
      logger.debug("Path " + path);
      inputStream = new FileInputStream(path);
    } catch (FileNotFoundException e) {
      try {
        URL urlpath = prop.getClass().getResource(filename);
        inputStream = new FileInputStream(urlpath.getPath());
      } catch (FileNotFoundException e1) {
        e1.printStackTrace();
      }
    }
    try {
      if (inputStream != null) {
        prop.load(inputStream);
        inputStream.close();
      }
    } catch (IOException IOE) {
      IOE.printStackTrace();
    }
    return prop;
  }

  /**
   * Gets the properties.
   * 
   * @return the properties
   */
  public Properties getProperties() {
    return properties;
  }
}
