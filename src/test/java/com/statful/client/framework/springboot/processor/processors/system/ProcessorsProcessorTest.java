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

public class ProcessorsProcessorTest extends AbstractProcessorTest {

    private ProcessorsProcessor subject;

    @Before
    public void before() {
        subject = new ProcessorsProcessor();
    }

    @Test
    public void shouldProcessMetric() {
        // Given
        ExportedMetric exportedMetric = new ExportedMetric.Builder()
                .withName("processors")
                .withTimestamp(EPOCH_SECONDS_PLUS_10_SECS)
                .withValue(METRIC_VALUE)
                .build();

        // When
        ProcessedMetric processedMetric = subject.process(exportedMetric);

        // Then
        assertEquals(SYSTEM_METRICS_PREFIX + "processors", processedMetric.getName());
        assertEquals(MetricType.GAUGE, processedMetric.getMetricType());
        assertEquals(Double.valueOf(METRIC_VALUE), processedMetric.getValue());
        assertEquals(EPOCH_SECONDS_PLUS_10_SECS, processedMetric.getTimestamp());
        assertFalse(processedMetric.getAggregations().isPresent());
        assertFalse(processedMetric.getTags().isPresent());
        assertFalse(processedMetric.getAggregationDetails().isPresent());
    }
}
