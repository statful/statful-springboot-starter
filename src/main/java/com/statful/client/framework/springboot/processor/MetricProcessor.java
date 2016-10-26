package com.statful.client.framework.springboot.processor;

import com.statful.client.framework.springboot.common.ExportedMetric;
import com.statful.client.framework.springboot.common.ProcessedMetric;

import java.util.List;

/**
 * Interface that define the contract for metric processors.
 */
public interface MetricProcessor {

    String SYSTEM_METRICS_PREFIX = "system.";
    String TOMCAT_METRICS_PREFIX = "tomcat.";
    String ACCUMULATED_METRICS_PREFIX = "accumulated.";
    String LATEST_METRICS_PREFIX = "latest.";

    /**
     * Processes that converts an {@link ExportedMetric} into a {@link ProcessedMetric}.
     * @param exportedMetric Exported metric to process
     * @return {@link ProcessedMetric} Processed metric
     */
    ProcessedMetric process(ExportedMetric exportedMetric);

    List<String> getProcessorKeys();
}
