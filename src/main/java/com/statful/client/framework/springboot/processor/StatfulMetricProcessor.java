package com.statful.client.framework.springboot.processor;

import com.statful.client.framework.springboot.common.ExportedMetric;
import com.statful.client.framework.springboot.common.ProcessedMetric;
import org.springframework.stereotype.Component;

@Component
public class StatfulMetricProcessor {

    public static boolean validate(ExportedMetric exportedMetric) {
        return ProcessorMap.validateProcessor(exportedMetric.getName());
    }

    public static ProcessedMetric process(ExportedMetric exportedMetric) {
        MetricProcessor metricProcessor = ProcessorMap.getProcessor(exportedMetric.getName());

        return metricProcessor.process(exportedMetric);
    }
}
