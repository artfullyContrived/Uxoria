package com.artfully.contrived.smpp.common;

import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;

import com.artfully.contrived.util.PropertyUtils;
import com.artfully.contrived.util.Props;

public abstract class Runner {

  protected void initializeLogger() {
    Properties log4jprops = PropertyUtils.getPropertiesFromFile(Props.log4jPropertyFile.getFileName());
    PropertyConfigurator.configure(log4jprops);
  }

  public abstract void start();

}
