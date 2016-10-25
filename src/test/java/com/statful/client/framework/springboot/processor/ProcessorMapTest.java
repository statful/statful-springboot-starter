package com.statful.client.framework.springboot.processor;

import com.statful.client.framework.springboot.processor.processors.system.GcProcessor;
import org.junit.Test;

import static org.junit.Assert.*;

public class ProcessorMapTest {

    @Test
    public void shouldValidateProcessor() {
        // Given
        String metric = "gc";

        // When
        boolean validated = ProcessorMap.validateProcessor(metric);

        // Then
        assertTrue(validated);
    }

    @Test
    public void shouldNotValidateProcessorIfInvalidMetric() {
        // Given
        String metric = "invalid";

        // When
        boolean validated = ProcessorMap.validateProcessor(metric);

        // Then
        assertFalse(validated);
    }

    @Test
    public void shouldReturnProperProcessor() {
        // Given
        String metric = "gc";

        // When
        MetricProcessor metricProcessor = ProcessorMap.getProcessor(metric);

        // Then
        assertEquals(GcProcessor.class, metricProcessor.getClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionIfInvalidMetric() {
        // Given
        String metric = "invalid";

        // When
        ProcessorMap.getProcessor(metric);
    }
}
