package com.statful.client.framework.springboot.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Class responsible to maintain a map of metric prefixes and correspondent processors.
 */
@Component
public class ProcessorMap {

    private Map<String, MetricProcessor> processors = new HashMap<>();

    /**
     * Sets entries in the processors map based on a list of Metric processors.
     *
     * @param injectedProcessors {@link List} List of {@link MetricProcessor} processors
     */
    @Autowired
    protected void setProcessors(List<MetricProcessor> injectedProcessors) {
        injectedProcessors
                .forEach(processor -> processor.getProcessorKeys().forEach(key -> processors.put(key, processor)));
    }

    /**
     * Returns the proper processor for a metric.
     *
     * @param metric {@link String} Metric name
     * @return {@link MetricProcessor} Processor that handles the particular metric
     */
    public MetricProcessor getProcessor(String metric) {
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

    public Map<String, MetricProcessor> getProcessors() {
        return new HashMap<>(processors);
    }
}
