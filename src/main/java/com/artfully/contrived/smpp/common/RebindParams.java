package com.artfully.contrived.smpp.common;

import static com.github.rholder.retry.StopStrategies.stopAfterAttempt;
import static com.github.rholder.retry.WaitStrategies.incrementingWait;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.net.ConnectException;
import java.util.Properties;

import com.github.rholder.retry.StopStrategy;
import com.github.rholder.retry.WaitStrategy;
import com.google.inject.Inject;

public class RebindParams {

  private final StopStrategy stopStrategy;
  private final WaitStrategy waitStrategy;;
  private final Class<? extends Exception> retryException;

  @Inject
  public RebindParams(Properties props) {

    this.stopStrategy = stopAfterAttempt(Integer.parseInt(props.getProperty("retry.attempts")));
    this.waitStrategy =
        incrementingWait(5, SECONDS, Integer.parseInt(props.getProperty("retry.waits")), SECONDS);
    this.retryException = ConnectException.class;
  }

  public StopStrategy getStopStrategy() {
    return stopStrategy;
  }

  public WaitStrategy getWaitStrategy() {
    return waitStrategy;
  }

  public Class<? extends Exception> getRetryException() {
    return retryException;
  }

}
