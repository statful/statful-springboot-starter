package com.statful.client.framework.springboot.processor.processors.system;

import com.statful.client.framework.springboot.common.ExportedMetric;
import com.statful.client.framework.springboot.common.MetricType;
import com.statful.client.framework.springboot.common.ProcessedMetric;
import org.junit.Before;
import org.junit.Test;

import java.time.Instant;
import java.util.Date;

import static com.statful.client.framework.springboot.processor.MetricProcessor.ACCUMULATED_METRICS_PREFIX;
import static com.statful.client.framework.springboot.processor.MetricProcessor.SYSTEM_METRICS_PREFIX;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class GcProcessorTest {

    private GcProcessor subject;

    @Before
    public void before() {
        subject = new GcProcessor();
    }

    @Test
    public void shouldProcessMetricCount() {
        // Given
        ExportedMetric exportedMetric = new ExportedMetric.Builder()
                .withName("gc.ps_scavenge.count")
                .withTimestamp(Date.from(Instant.EPOCH))
                .withValue(1D)
                .build();

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

    @Test
    public void shouldProcessMetricGauge() {
        // Given
        ExportedMetric exportedMetric = new ExportedMetric.Builder()
                .withName("gc.ps_scavenge.time")
                .withTimestamp(Date.from(Instant.EPOCH))
                .withValue(1D)
                .build();

        // When
        ProcessedMetric processedMetric = subject.process(exportedMetric);

        // Then
        assertEquals(SYSTEM_METRICS_PREFIX + ACCUMULATED_METRICS_PREFIX + "gc", processedMetric.getName());
        assertEquals(MetricType.TIMER, processedMetric.getMetricType());
        assertEquals(Double.valueOf(1D), processedMetric.getValue());
        assertEquals(Instant.EPOCH.toEpochMilli(), processedMetric.getTimestamp());
        assertEquals("ps_scavenge", processedMetric.getTags().get().getTagValue("name"));
        assertFalse(processedMetric.getAggregations().isPresent());
        assertFalse(processedMetric.getAggregationDetails().isPresent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionIfInvalidMetricDueToTooManyParts() {
        // Given
        ExportedMetric exportedMetric = new ExportedMetric.Builder()
                .withName("this.is.an.invalid.metric.for.sure")
                .build();

        // When
        subject.process(exportedMetric);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionIfInvalidMetricDueToUnknownName() {
        // Given
        ExportedMetric exportedMetric = new ExportedMetric.Builder()
                .withName("gc.invalid.name")
                .build();

        // When
        subject.process(exportedMetric);
    }
}
