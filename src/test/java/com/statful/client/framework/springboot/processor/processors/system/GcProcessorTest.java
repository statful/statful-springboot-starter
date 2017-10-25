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

public class GcProcessorTest extends GcProcessorBaseTest {

    private GcProcessor subject;

    @Before
    public void before() {
        subject = new GcProcessor();
    }

    @Test
    public void shouldProcessMetricCount() {

        this.shouldProcessMetricCount(subject, SYSTEM_METRICS_PREFIX + ACCUMULATED_METRICS_PREFIX + "gc");

    }

    @Test
    public void shouldProcessMetricGauge() {
        this.shouldProcessMetricGauge(subject, SYSTEM_METRICS_PREFIX + ACCUMULATED_METRICS_PREFIX + "gc");
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
