package com.statful.client.framework.springboot.processor.processors.system;

import com.statful.client.framework.springboot.common.ExportedMetric;
import com.statful.client.framework.springboot.common.MetricType;
import com.statful.client.framework.springboot.common.ProcessedMetric;
import com.statful.client.framework.springboot.processor.AbstractProcessorTest;
import com.statful.client.framework.springboot.processor.MetricProcessor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class GcProcessorBaseTest extends AbstractProcessorTest {

    void shouldProcessMetricCount(MetricProcessor subject, String assertName) {
        // Given
        ExportedMetric exportedMetric = new ExportedMetric.Builder()
                .withName("gc.ps_scavenge.count")
                .withTimestamp(EPOCH_SECONDS_PLUS_10_SECS)
                .withValue(METRIC_VALUE)
                .build();

        // When
        ProcessedMetric processedMetric = subject.process(exportedMetric);

        // Then
        assertEquals(assertName, processedMetric.getName());
        assertEquals(MetricType.COUNTER, processedMetric.getMetricType());
        assertEquals(Double.valueOf(METRIC_VALUE), processedMetric.getValue());
        assertEquals(EPOCH_SECONDS_PLUS_10_SECS, processedMetric.getTimestamp());
        assertEquals("ps_scavenge", processedMetric.getTags().get().getTagValue("name"));
        assertFalse(processedMetric.getAggregations().isPresent());
        assertFalse(processedMetric.getAggregationDetails().isPresent());
    }

    public void shouldProcessMetricGauge(MetricProcessor subject, String assertName) {
        // Given
        ExportedMetric exportedMetric = new ExportedMetric.Builder()
                .withName("gc.ps_scavenge.time")
                .withTimestamp(EPOCH_SECONDS_PLUS_10_SECS)
                .withValue(METRIC_VALUE)
                .build();

        // When
        ProcessedMetric processedMetric = subject.process(exportedMetric);

        // Then
        assertEquals(assertName, processedMetric.getName());
        assertEquals(MetricType.TIMER, processedMetric.getMetricType());
        assertEquals(Double.valueOf(METRIC_VALUE), processedMetric.getValue());
        assertEquals(EPOCH_SECONDS_PLUS_10_SECS, processedMetric.getTimestamp());
        assertEquals("ps_scavenge", processedMetric.getTags().get().getTagValue("name"));
        assertFalse(processedMetric.getAggregations().isPresent());
        assertFalse(processedMetric.getAggregationDetails().isPresent());
    }
}
