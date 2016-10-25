package com.statful.client.framework.springboot.processor;

import com.statful.client.framework.springboot.common.ExportedMetric;
import com.statful.client.framework.springboot.common.ProcessedMetric;
import org.springframework.stereotype.Component;

@Component
public class StatfulMetricProcessor {

    public boolean validate(ExportedMetric exportedMetric) {
        return ProcessorMap.validateProcessor(exportedMetric.getName());
    }

    public ProcessedMetric process(ExportedMetric exportedMetric) {
        MetricProcessor metricProcessor = ProcessorMap.getProcessor(exportedMetric.getName());

        return metricProcessor.process(exportedMetric);
    }
}
