package com.statful.client.framework.springboot.processor;

import com.statful.client.framework.springboot.common.ExportedMetric;
import com.statful.client.framework.springboot.common.MetricType;
import com.statful.client.framework.springboot.common.ProcessedMetric;
import org.junit.Before;
import org.junit.Test;

import java.time.Instant;
import java.util.Date;

import static com.statful.client.framework.springboot.processor.MetricProcessor.ACCUMULATED_METRICS_PREFIX;
import static com.statful.client.framework.springboot.processor.MetricProcessor.SYSTEM_METRICS_PREFIX;
import static org.junit.Assert.*;

public class StatfulMetricProcessorTest {

    private static ExportedMetric exportedMetric;
    private StatfulMetricProcessor subject;

    @Before
    public void before() {
        subject = new StatfulMetricProcessor();
        exportedMetric = new ExportedMetric.Builder()
                .withName("gc.ps_scavenge.count")
                .withValue(1D)
                .withTimestamp(Date.from(Instant.EPOCH))
                .build();
    }

    @Test
    public void shouldValidateProcessor() {
        // When
        boolean validated = subject.validate(exportedMetric);

        // Then
        assertTrue(validated);
    }

    @Test
    public void shouldNotValidateProcessorIfInvalidMetric() {
        // Given
        ExportedMetric exportedMetric = new ExportedMetric.Builder()
                .withName("invalid")
                .build();

        // When
        boolean validated = subject.validate(exportedMetric);

        // Then
        assertFalse(validated);
    }

    @Test
    public void shouldReturnProperProcessor() {
        // When
        ProcessedMetric processedMetric = subject.process(exportedMetric);

        // Then
        assertEquals(SYSTEM_METRICS_PREFIX + ACCUMULATED_METRICS_PREFIX + "gc", processedMetric.getName());
        assertEquals(MetricType.COUNTER, processedMetric.getMetricType());
        assertEquals(Double.valueOf(1D), processedMetric.getValue());
        assertEquals(Instant.EPOCH.toEpochMilli(), processedMetric.getTimestamp());
        assertEquals("ps_scavenge", processedMetric.getTags().get().getTagValue("name"));
        assertFalse(processedMetric.getAggregations().isPresent());
        assertFalse(processedMetric.getAggregationDetails().isPresent());
    }

}
