package com.statful.client.framework.springboot.processor.processors.system;

import com.statful.client.domain.api.Aggregation;
import com.statful.client.domain.api.AggregationFrequency;
import com.statful.client.framework.springboot.common.ExportedMetric;
import com.statful.client.framework.springboot.common.MetricType;
import com.statful.client.framework.springboot.common.ProcessedMetric;
import org.junit.Before;
import org.junit.Test;

import java.time.Instant;

import static com.statful.client.framework.springboot.processor.MetricProcessor.SYSTEM_METRICS_PREFIX;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class SystemLoadProcessorTest {

    private SystemLoadProcessor subject;

    @Before
    public void before() {
        subject = new SystemLoadProcessor();
    }

    @Test
    public void shouldProcessMetric() {
        // Given
        ExportedMetric exportedMetric = new ExportedMetric.Builder()
                .withName("systemload.average")
                .withTimestamp(Instant.EPOCH.plusSeconds(10).getEpochSecond())
                .withValue(1D)
                .build();

        // When
        ProcessedMetric processedMetric = subject.process(exportedMetric);

        // Then
        assertEquals(SYSTEM_METRICS_PREFIX + "systemload", processedMetric.getName());
        assertEquals(MetricType.GAUGE, processedMetric.getMetricType());
        assertEquals(Double.valueOf(1D), processedMetric.getValue());
        assertEquals(Instant.EPOCH.plusSeconds(10).getEpochSecond(), processedMetric.getTimestamp());
        assertFalse(processedMetric.getTags().isPresent());
        assertFalse(processedMetric.getAggregations().isPresent());
        assertEquals(Aggregation.AVG, processedMetric.getAggregationDetails().get().getAggregation());
        assertEquals(AggregationFrequency.FREQ_60, processedMetric.getAggregationDetails().get().getAggregationFrequency());
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
    public void shouldThrowIllegalArgumentExceptionIfInvalidMetricDueToNotEnoughParts() {
        // Given
        ExportedMetric exportedMetric = new ExportedMetric.Builder()
                .withName("invalid")
                .build();

        // When
        subject.process(exportedMetric);
    }
}
