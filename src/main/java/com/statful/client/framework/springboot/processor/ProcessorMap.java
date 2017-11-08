package com.statful.client.framework.springboot.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Class responsible to maintain a map of metric prefixes and correspondent processors.
 */
@Component
public class ProcessorMap {

    private Map<String, List<MetricProcessor>> processors = new HashMap<>();

    /**
     * Sets entries in the processors map based on a list of Metric processors.
     *
     * @param injectedProcessors {@link List} List of {@link MetricProcessor} processors
     */
    @Autowired
    protected void setProcessors(List<MetricProcessor> injectedProcessors) {
        injectedProcessors.forEach(processor -> processor.getProcessorKeys().forEach(key -> {
            processors.compute(key, (s, metricProcessors) -> {
                if (metricProcessors == null) {
                    metricProcessors = new ArrayList<>();
                }
                metricProcessors.add(processor);
                return metricProcessors;
            });
        }));
    }

    /**
     * Returns a list of processors for a metric.
     *
     * @param metric {@link String} Metric name
     * @return {@link MetricProcessor} Processor that handles the particular metric
     */
    public List<MetricProcessor> getProcessors(String metric) {
        Optional<String> mapKey = processors.keySet().stream()
                .filter(metric::startsWith)
                .findFirst();

        return mapKey.map(processors::get).orElseThrow(IllegalArgumentException::new);
    }

    /**
     * Validates if there is a processor for a particular metric.
     *
     * @param metric {@link String} Metric name.
     * @return {@link Boolean} True if a processor exist for a particular metric
     */
    public boolean validateProcessor(String metric) {
        return processors.keySet().stream().anyMatch(metric::startsWith);
    }

    public Map<String, List<MetricProcessor>> getProcessors() {
        return new HashMap<>(processors);
    }
}
