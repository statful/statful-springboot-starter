package com.statful.client.framework.springboot.processor;

import com.statful.client.framework.springboot.processor.processors.http.HttpRequestsProcessor;
import com.statful.client.framework.springboot.processor.processors.system.*;
import com.statful.client.framework.springboot.processor.processors.tomcat.HttpSessionsProcessor;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class ProcessorMapTest {

    private ProcessorMap subject;

    @Before
    public void before() {
        subject = new ProcessorMap();
        subject.setProcessors(Collections.singletonList(new GcProcessor()));
    }

    @Test
    public void shouldValidateProcessor() {
        // Given
        String metric = "gc";

        // When
        boolean validated = subject.validateProcessor(metric);

        // Then
        assertTrue(validated);
    }

    @Test
    public void shouldNotValidateProcessorIfInvalidMetric() {
        // Given
        String metric = "invalid";

        // When
        boolean validated = subject.validateProcessor(metric);

        // Then
        assertFalse(validated);
    }

    @Test
    public void shouldReturnProperProcessor() {
        // Given
        String metric = "gc";

        // When
        List<MetricProcessor> metricProcessors = subject.getProcessors(metric);

        // Then
        metricProcessors.forEach(metricProcessor -> assertEquals(GcProcessor.class, metricProcessor.getClass()));
    }

    @Test
    public void shouldSetMultipleProcessors() {
        // Given
        List<MetricProcessor> metricProcessors = new ArrayList<>();
        metricProcessors.add(new HttpRequestsProcessor());
        metricProcessors.add(new ClassesProcessor());
        metricProcessors.add(new GcProcessor());
        metricProcessors.add(new GcDiffProcessor());
        metricProcessors.add(new HeapProcessor());
        metricProcessors.add(new MemProcessor());
        metricProcessors.add(new ProcessorsProcessor());
        metricProcessors.add(new SystemLoadProcessor());
        metricProcessors.add(new ThreadsProcessor());
        metricProcessors.add(new UptimeProcessor());
        metricProcessors.add(new HttpSessionsProcessor());

        // When
        subject.setProcessors(metricProcessors);

        // Then
        assertEquals("Number of mappers should be expected", 13, subject.getProcessors().size());
        assertEquals(HttpRequestsProcessor.class, subject.getProcessors().get("counter.status").get(0).getClass());
        assertEquals(HttpRequestsProcessor.class, subject.getProcessors().get("gauge.response").get(0).getClass());
        assertEquals(GcProcessor.class, subject.getProcessors().get("gc").get(0).getClass());

    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionIfInvalidMetric() {
        // Given
        String metric = "invalid";

        // When
        subject.getProcessors(metric);
    }

}
