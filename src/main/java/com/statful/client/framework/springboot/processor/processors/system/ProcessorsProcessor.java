package com.statful.client.framework.springboot.processor.processors.system;

import com.statful.client.framework.springboot.common.ExportedMetric;
import com.statful.client.framework.springboot.common.MetricType;
import com.statful.client.framework.springboot.common.ProcessedMetric;
import com.statful.client.framework.springboot.processor.GenericProcessor;
import com.statful.client.framework.springboot.processor.MetricProcessor;

public class ProcessorsProcessor implements MetricProcessor {

    @Override
    public ProcessedMetric process(ExportedMetric exportedMetric) {
        // Example: processors=8
        return GenericProcessor.process(SYSTEM_METRICS_PREFIX + exportedMetric.getName(),
                MetricType.GAUGE, exportedMetric.getValue().doubleValue(), exportedMetric.getTimestamp().getTime());
    }
}
