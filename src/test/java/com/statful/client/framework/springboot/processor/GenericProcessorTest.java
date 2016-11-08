package com.statful.client.framework.springboot.processor;

import com.statful.client.framework.springboot.common.MetricType;
import com.statful.client.framework.springboot.common.ProcessedMetric;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class GenericProcessorTest {

    @Test
    public void shouldProcessMetric() {
        // Given
        String name = "foo";
        MetricType type = MetricType.COUNTER;
        double value = 1D;
        long timestamp = 123456789L;

        // When
        ProcessedMetric processedMetric = GenericProcessor.process(name, type, value, timestamp);

        // Then
        assertEquals(name, processedMetric.getName());
        assertEquals(type, processedMetric.getMetricType());
        assertEquals(Double.valueOf(value), processedMetric.getValue());
        assertEquals(timestamp, processedMetric.getTimestamp());
        assertFalse(processedMetric.getAggregations().isPresent());
        assertFalse(processedMetric.getTags().isPresent());
        assertFalse(processedMetric.getAggregationDetails().isPresent());
    }
}
