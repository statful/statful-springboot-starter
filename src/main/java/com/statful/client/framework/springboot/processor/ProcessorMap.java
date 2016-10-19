package com.statful.client.framework.springboot.processor;

import com.statful.client.framework.springboot.processor.processors.http.HttpRequestsProcessor;
import com.statful.client.framework.springboot.processor.processors.system.*;
import com.statful.client.framework.springboot.processor.processors.tomcat.HttpSessionsProcessor;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class ProcessorMap {

    private static Map<String, MetricProcessor> processors;

    static {
        processors = new HashMap<>();
        processors.put("mem", new MemProcessor());
        processors.put("processors", new ProcessorsProcessor());
        processors.put("instance", new UptimeProcessor());
        processors.put("uptime", new UptimeProcessor());
        processors.put("systemload", new SystemLoadProcessor());
        processors.put("heap", new HeapProcessor());
        processors.put("nonheap", new HeapProcessor());
        processors.put("threads", new ThreadsProcessor());
        processors.put("classes", new ClassesProcessor());
        processors.put("gc", new GcProcessor());
        processors.put("httpsessions", new HttpSessionsProcessor());
        processors.put("counter.status", new HttpRequestsProcessor());
        processors.put("gauge.response", new HttpRequestsProcessor());
    }

    public static MetricProcessor getProcessor(String metric) {
        Optional<String> mapKey = processors.keySet().stream()
                .filter(metric::startsWith)
                .findFirst();

        if (mapKey.isPresent()) {
            return processors.get(mapKey.get());
        } else {
            throw new IllegalArgumentException();
        }
    }

    public static boolean validateProcessor(String metric) {
        return processors.keySet().stream().anyMatch(metric::startsWith);
    }
}
