package com.artfully.contrived.smpp.receiver.module;

import java.util.Properties;

import com.artfully.contrived.smpp.common.Runner;
import com.artfully.contrived.smpp.receiver.MainReceiver;
import com.artfully.contrived.util.PropertyUtils;
import com.artfully.contrived.util.Props;
import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

public class ReceiverModule extends AbstractModule {

  private EventBus eventBus = new EventBus("receiver-eventbus");

  @Override
  protected void configure() {

    Properties props = new Properties();
    props = PropertyUtils.getPropertiesFromFile(Props.receiverPropertyFile.getFileName());

    bind(Properties.class).toInstance(props);
    bind(EventBus.class).toInstance(eventBus);
    bind(Runner.class).to(MainReceiver.class);

    install(new FactoryModuleBuilder().build(SessionBinderFactory.class));

  }
}
