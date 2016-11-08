package com.statful.client.framework.springboot.processor.processors.tomcat;

import com.statful.client.framework.springboot.common.ExportedMetric;
import com.statful.client.framework.springboot.common.MetricType;
import com.statful.client.framework.springboot.common.ProcessedMetric;
import org.junit.Before;
import org.junit.Test;

import java.time.Instant;

import static com.statful.client.framework.springboot.processor.MetricProcessor.TOMCAT_METRICS_PREFIX;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class HttpSessionsProcessorTest {

    private HttpSessionsProcessor subject;

    @Before
    public void before() {
        subject = new HttpSessionsProcessor();
    }

    @Test
    public void shouldProcessMetric() {
        // Given
        ExportedMetric exportedMetric = new ExportedMetric.Builder()
                .withName("httpsessions.max")
                .withTimestamp(Instant.EPOCH.plusSeconds(10).getEpochSecond())
                .withValue(1D)
                .build();

        // When
        ProcessedMetric processedMetric = subject.process(exportedMetric);

        // Then
        assertEquals(TOMCAT_METRICS_PREFIX + "httpsessions", processedMetric.getName());
        assertEquals(MetricType.GAUGE, processedMetric.getMetricType());
        assertEquals(Double.valueOf(1D), processedMetric.getValue());
        assertEquals(Instant.EPOCH.plusSeconds(10).getEpochSecond(), processedMetric.getTimestamp());
        assertEquals("max", processedMetric.getTags().get().getTagValue("type"));
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
