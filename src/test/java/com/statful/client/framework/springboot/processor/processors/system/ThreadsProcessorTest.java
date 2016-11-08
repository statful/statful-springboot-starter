package com.statful.client.framework.springboot.processor.processors.system;

import com.statful.client.framework.springboot.common.ExportedMetric;
import com.statful.client.framework.springboot.common.MetricType;
import com.statful.client.framework.springboot.common.ProcessedMetric;
import com.statful.client.framework.springboot.processor.AbstractProcessorTest;
import org.junit.Before;
import org.junit.Test;

import static com.statful.client.framework.springboot.processor.MetricProcessor.SYSTEM_METRICS_PREFIX;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class ThreadsProcessorTest extends AbstractProcessorTest {

    private ThreadsProcessor subject;

    @Before
    public void before() {
        subject = new ThreadsProcessor();
    }

    @Test
    public void shouldProcessMetricWithoutType() {
        // Given
        ExportedMetric exportedMetric = new ExportedMetric.Builder()
                .withName("threads")
                .withTimestamp(EPOCH_SECONDS_PLUS_10_SECS)
                .withValue(METRIC_VALUE)
                .build();

        // When
        ProcessedMetric processedMetric = subject.process(exportedMetric);

        // Then
        assertEquals(SYSTEM_METRICS_PREFIX + "threads", processedMetric.getName());
        assertEquals(MetricType.GAUGE, processedMetric.getMetricType());
        assertEquals(Double.valueOf(METRIC_VALUE), processedMetric.getValue());
        assertEquals(EPOCH_SECONDS_PLUS_10_SECS, processedMetric.getTimestamp());
        assertEquals("total", processedMetric.getTags().get().getTagValue("type"));
        assertFalse(processedMetric.getAggregations().isPresent());
        assertFalse(processedMetric.getAggregationDetails().isPresent());
    }

    @Test
    public void shouldProcessMetricWithType() {
        // Given
        ExportedMetric exportedMetric = new ExportedMetric.Builder()
                .withName("threads.peak")
                .withTimestamp(EPOCH_SECONDS_PLUS_10_SECS)
                .withValue(METRIC_VALUE)
                .build();

        // When
        ProcessedMetric processedMetric = subject.process(exportedMetric);

        // Then
        assertEquals(SYSTEM_METRICS_PREFIX + "threads", processedMetric.getName());
        assertEquals(MetricType.GAUGE, processedMetric.getMetricType());
        assertEquals(Double.valueOf(METRIC_VALUE), processedMetric.getValue());
        assertEquals(EPOCH_SECONDS_PLUS_10_SECS, processedMetric.getTimestamp());
        assertEquals("peak", processedMetric.getTags().get().getTagValue("type"));
        assertFalse(processedMetric.getAggregations().isPresent());
        assertFalse(processedMetric.getAggregationDetails().isPresent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionIfInvalidMetric() {
        // Given
        ExportedMetric exportedMetric = new ExportedMetric.Builder()
                .withName("this.is.an.invalid.metric.for.sure")
                .build();

        // When
        subject.process(exportedMetric);
    }
}
