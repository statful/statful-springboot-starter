package com.statful.client.framework.springboot.processor.processors.system;

import com.statful.client.framework.springboot.common.ExportedMetric;
import com.statful.client.framework.springboot.common.MetricType;
import com.statful.client.framework.springboot.common.ProcessedMetric;
import com.statful.client.framework.springboot.processor.AbstractProcessorTest;
import org.junit.Before;
import org.junit.Test;

import static com.statful.client.framework.springboot.processor.MetricProcessor.ACCUMULATED_METRICS_PREFIX;
import static com.statful.client.framework.springboot.processor.MetricProcessor.SYSTEM_METRICS_PREFIX;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class GcDiffProcessorTest extends GcProcessorBaseTest {

    private GcDiffProcessor subject;

    @Before
    public void before() {
        subject = new GcDiffProcessor();
    }

    @Test
    public void shouldProcessMetricCount() {

        this.shouldProcessMetricCount(subject, SYSTEM_METRICS_PREFIX + "gc");
    }

    @Test
    public void shouldProcessMetricGauge() {

        this.shouldProcessMetricGauge(subject, SYSTEM_METRICS_PREFIX + "gc");
    }

    @Test
    public void shouldProcessMetricCountAndProduceDiff() {

        // Given
        ExportedMetric exportedMetric1 = new ExportedMetric.Builder()
                .withName("gc.ps_scavenge.count")
                .withTimestamp(EPOCH_SECONDS_PLUS_10_SECS)
                .withValue(1D)
                .build();

        ExportedMetric exportedMetric2 = new ExportedMetric.Builder()
                .withName("gc.ps_scavenge.count")
                .withTimestamp(EPOCH_SECONDS_PLUS_10_SECS)
                .withValue(5D)
                .build();

        // When
        subject.process(exportedMetric1);
        ProcessedMetric processedMetric = subject.process(exportedMetric2);
        assertEquals(Double.valueOf(4.0), processedMetric.getValue());


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
