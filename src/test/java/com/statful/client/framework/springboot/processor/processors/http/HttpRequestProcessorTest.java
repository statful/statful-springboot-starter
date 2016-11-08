package com.statful.client.framework.springboot.processor.processors.http;

import com.statful.client.framework.springboot.common.ExportedMetric;
import com.statful.client.framework.springboot.common.MetricType;
import com.statful.client.framework.springboot.common.ProcessedMetric;
import com.statful.client.framework.springboot.processor.AbstractProcessorTest;
import org.junit.Before;
import org.junit.Test;

import static com.statful.client.framework.springboot.processor.MetricProcessor.ACCUMULATED_METRICS_PREFIX;
import static com.statful.client.framework.springboot.processor.MetricProcessor.LATEST_METRICS_PREFIX;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class HttpRequestProcessorTest extends AbstractProcessorTest {

    private HttpRequestsProcessor subject;

    @Before
    public void before() {
        subject = new HttpRequestsProcessor();
    }

    @Test
    public void shouldProcessMetricCounter() {
        // Given
        ExportedMetric exportedMetric = new ExportedMetric.Builder()
                .withName("counter.status.200.star-star.favicon.ico")
                .withTimestamp(EPOCH_SECONDS_PLUS_10_SECS)
                .withValue(METRIC_VALUE)
                .build();

        // When
        ProcessedMetric processedMetric = subject.process(exportedMetric);

        // Then
        assertEquals(ACCUMULATED_METRICS_PREFIX + "requests", processedMetric.getName());
        assertEquals(MetricType.COUNTER, processedMetric.getMetricType());
        assertEquals(Double.valueOf(METRIC_VALUE), processedMetric.getValue());
        assertEquals(EPOCH_SECONDS_PLUS_10_SECS, processedMetric.getTimestamp());
        assertEquals("200", processedMetric.getTags().get().getTagValue("status"));
        assertEquals("star-star.favicon.ico", processedMetric.getTags().get().getTagValue("url"));
        assertFalse(processedMetric.getAggregations().isPresent());
        assertFalse(processedMetric.getAggregationDetails().isPresent());
    }

    @Test
    public void shouldProcessMetricGauge() {
        // Given
        ExportedMetric exportedMetric = new ExportedMetric.Builder()
                .withName("gauge.response.star-star.favicon.ico")
                .withTimestamp(EPOCH_SECONDS_PLUS_10_SECS)
                .withValue(METRIC_VALUE)
                .build();

        // When
        ProcessedMetric processedMetric = subject.process(exportedMetric);

        // Then
        assertEquals(LATEST_METRICS_PREFIX + "responses", processedMetric.getName());
        assertEquals(MetricType.TIMER, processedMetric.getMetricType());
        assertEquals(Double.valueOf(METRIC_VALUE), processedMetric.getValue());
        assertEquals(EPOCH_SECONDS_PLUS_10_SECS, processedMetric.getTimestamp());
        assertEquals("star-star.favicon.ico", processedMetric.getTags().get().getTagValue("url"));
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
