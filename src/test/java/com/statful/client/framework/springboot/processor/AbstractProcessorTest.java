package com.statful.client.framework.springboot.processor;

import java.time.Instant;

public abstract class AbstractProcessorTest {
    protected final static long EPOCH_SECONDS_PLUS_10_SECS = Instant.EPOCH.plusSeconds(10).getEpochSecond();
    protected final static double METRIC_VALUE = 1D;
}
