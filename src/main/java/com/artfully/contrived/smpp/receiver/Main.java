package com.artfully.contrived.smpp.receiver;

import com.artfully.contrived.smpp.common.Runner;
import com.artfully.contrived.smpp.common.SessionBinder;
import com.artfully.contrived.smpp.receiver.module.ReceiverModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class Main {

  public static void main(String[] args) {

    Injector injector = Guice.createInjector(new ReceiverModule());
    injector.injectMembers(SessionBinder.class);
    Runner receiver = injector.getInstance(Runner.class);

    receiver.start();
  }
}
