package com.artfully.contrived.smpp.common;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.net.ConnectException;
import java.util.Properties;

import com.github.rholder.retry.StopStrategies;
import com.github.rholder.retry.StopStrategy;
import com.github.rholder.retry.WaitStrategies;
import com.github.rholder.retry.WaitStrategy;

public class RebindParams {
	private final StopStrategy stopStrategy;
	private final WaitStrategy waitStrategy;;
	private final Class<? extends Exception> retryException;

	public RebindParams(Properties props) {

		this.stopStrategy = StopStrategies.stopAfterAttempt(Integer
				.parseInt(props.getProperty("retry.attempts")));
		this.waitStrategy = WaitStrategies.incrementingWait(5, SECONDS,
				Integer.parseInt(props.getProperty("retry.waits")), SECONDS);
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