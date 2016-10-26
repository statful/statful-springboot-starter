package com.statful.client.framework.springboot.processor;

import com.statful.client.framework.springboot.common.MetricType;
import com.statful.client.framework.springboot.common.ProcessedMetric;

/**
 * Generic processor responsible for parsing exported metrics.
 *
 * Only useful as an util method.
 */
public final class GenericProcessor {

    private GenericProcessor() {}

    /**
     * Processes metric details into a Processed metric.
     *
     * @param name {@link String} Metric name
     * @param metricType {@link MetricType} Metric type
     * @param value {@link Double} Metric value
     * @param timestamp {@link long} Metric timestamp
     * @return {@link ProcessedMetric} Processed metric
     */
    public static ProcessedMetric process(String name, MetricType metricType, Double value, long timestamp) {
        return new ProcessedMetric.Builder().withName(name)
                .withMetricType(metricType)
                .withValue(value)
                .withTimestamp(timestamp)
                .build();
    }
}
