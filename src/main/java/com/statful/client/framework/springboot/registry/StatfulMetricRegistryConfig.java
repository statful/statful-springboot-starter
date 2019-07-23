package com.statful.client.framework.springboot.registry;

import com.google.common.annotations.VisibleForTesting;
import io.micrometer.core.instrument.step.StepRegistryConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;


/**
 * Configuration for {@link StatfulMetricRegistry}
 */
@Component
public class StatfulMetricRegistryConfig implements StepRegistryConfig {

    @Value("${statful.springboot.metrics.prefix:}")
    private String prefix;
    @Value("${statful.springboot.metrics.step:10}")
    private int step;
    @Value("${statful.springboot.metrics.enabled:false}")
    private boolean enabled;
    @Value("${statful.springboot.metrics.numThreads:0}")
    private int numThreads;

    @Override
    public String prefix() {
        return prefix;
    }

    @Override
    public String get(String key) {
        return null;
    }

    @Override
    public Duration step() {
        return Duration.ofSeconds(step);
    }

    @Override
    public boolean enabled() {
        return enabled;
    }

    @Override
    public int numThreads() {
        return numThreads;
    }

    @VisibleForTesting
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    @VisibleForTesting
    public void setStep(int step) {
        this.step = step;
    }

    @VisibleForTesting
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @VisibleForTesting
    public void setNumThreads(int numThreads) {
        this.numThreads = numThreads;
    }
}
