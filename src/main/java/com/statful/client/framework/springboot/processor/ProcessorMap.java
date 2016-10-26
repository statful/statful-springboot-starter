package com.statful.client.framework.springboot.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class ProcessorMap {

    private Map<String, MetricProcessor> processors = new HashMap<>();

    @Autowired
    protected void setProcessors(List<MetricProcessor> injectedProcessors) {
        injectedProcessors
                .forEach(processor -> processor.getProcessorKeys().forEach(key -> processors.put(key, processor)));
    }

    public MetricProcessor getProcessor(String metric) {
        Optional<String> mapKey = processors.keySet().stream()
                .filter(metric::startsWith)
                .findFirst();

        return mapKey.map(processors::get).orElseThrow(IllegalArgumentException::new);
    }

    public boolean validateProcessor(String metric) {
        return processors.keySet().stream().anyMatch(metric::startsWith);
    }

    public Map<String, MetricProcessor> getProcessors() {
        return new HashMap<>(processors);
    }
}
