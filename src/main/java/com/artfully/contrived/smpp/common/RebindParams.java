package com.artfully.contrived.smpp.common;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.net.ConnectException;
import java.util.Properties;

import com.artfully.contrived.smpp.dtos.SMPP;
import com.google.common.base.Predicate;
import com.inmobia.utils.retryer.Attempt;
import com.inmobia.utils.retryer.ExceptionClassPredicate;
import com.inmobia.utils.retryer.StopStrategies;
import com.inmobia.utils.retryer.StopStrategy;
import com.inmobia.utils.retryer.WaitStrategies;
import com.inmobia.utils.retryer.WaitStrategy;

public class RebindParams {
    private final StopStrategy stopStrategy;
    private final WaitStrategy waitStrategy;;
    private final Predicate<Attempt<SMPP>> rejectPredicate;

    public StopStrategy getStopStrategy() {
        return stopStrategy;
    }

    public WaitStrategy getWaitStrategy() {
        return waitStrategy;
    }

    public Predicate<Attempt<SMPP>> getRejectPredicate() {
        return rejectPredicate;
    }

    public RebindParams(Properties props) {

        this.stopStrategy = StopStrategies.stopAfterAttempt(Integer
    	    .parseInt(props.getProperty("retry.attempts")));
        this.waitStrategy = WaitStrategies.incrementingWait(5, SECONDS,
    	    Integer.parseInt(props.getProperty("retry.waits")),
    	    SECONDS);
        this.rejectPredicate = new ExceptionClassPredicate<SMPP>(
    	    ConnectException.class);
    }
}