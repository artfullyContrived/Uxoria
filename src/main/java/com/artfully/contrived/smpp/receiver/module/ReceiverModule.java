package com.artfully.contrived.smpp.receiver.module;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import com.artfully.contrived.smpp.receiver.MainReceiver;
import com.artfully.contrived.util.Props;
import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

public class ReceiverModule extends AbstractModule {

  private EventBus eventBus = new EventBus("receiver-eventbus");

  @Override
  protected void configure() {

    Properties defaults = new Properties();

    try {
      Properties props = new Properties(defaults);
      props.load(new FileInputStream(Props.receiverPropertyFile.getFileName()));
      bind(Properties.class).toInstance(props);
      System.out.println("Prps " + props);
    } catch (IOException e) {
      e.printStackTrace();
    }

    bind(MainReceiver.class);
    bind(EventBus.class).toInstance(eventBus);

    install(new FactoryModuleBuilder().build(SessionBinderFactory.class));

  }
}
