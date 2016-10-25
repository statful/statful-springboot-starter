package com.statful.client.framework.springboot.processor;

import com.statful.client.framework.springboot.common.MetricType;
import com.statful.client.framework.springboot.common.ProcessedMetric;

public abstract class GenericProcessor {

    public static ProcessedMetric process(String name, MetricType metricType, Double value, long timestamp) {
        return new ProcessedMetric.Builder().withName(name)
                .withMetricType(metricType)
                .withValue(value)
                .withTimestamp(timestamp)
                .build();
    }
}
