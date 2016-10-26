package com.statful.client.framework.springboot.processor;

import com.statful.client.framework.springboot.common.ExportedMetric;
import com.statful.client.framework.springboot.common.ProcessedMetric;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class StatfulMetricProcessor {

    @Resource
    private ProcessorMap processorMap;

    public boolean validate(ExportedMetric exportedMetric) {
        return processorMap.validateProcessor(exportedMetric.getName());
    }

    public ProcessedMetric process(ExportedMetric exportedMetric) {
        MetricProcessor metricProcessor = processorMap.getProcessor(exportedMetric.getName());

        return metricProcessor.process(exportedMetric);
    }

    public void setProcessorMap(ProcessorMap processorMap) {
        this.processorMap = processorMap;
    }
}
