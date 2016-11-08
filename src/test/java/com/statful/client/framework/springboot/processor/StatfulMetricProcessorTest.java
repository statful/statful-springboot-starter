package com.statful.client.framework.springboot.processor;

import com.statful.client.framework.springboot.common.ExportedMetric;
import com.statful.client.framework.springboot.common.MetricType;
import com.statful.client.framework.springboot.common.ProcessedMetric;
import com.statful.client.framework.springboot.processor.processors.system.GcProcessor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.time.Instant;
import java.util.Date;

import static com.statful.client.framework.springboot.processor.MetricProcessor.ACCUMULATED_METRICS_PREFIX;
import static com.statful.client.framework.springboot.processor.MetricProcessor.SYSTEM_METRICS_PREFIX;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class StatfulMetricProcessorTest {

    private static final String GC_PS_SCAVENGE_COUNT = "gc.ps_scavenge.count";
    private static final String INVALID = "invalid";
    private static ExportedMetric EXPORTED_METRIC;

    @Mock
    private ProcessorMap processorMap;

    private StatfulMetricProcessor subject;

    @Before
    public void before() {
        initMocks(this);

        when(processorMap.validateProcessor(GC_PS_SCAVENGE_COUNT)).thenReturn(true);
        when(processorMap.validateProcessor(INVALID)).thenReturn(false);
        when(processorMap.getProcessor(GC_PS_SCAVENGE_COUNT)).thenReturn(new GcProcessor());

        subject = new StatfulMetricProcessor();
        subject.setProcessorMap(processorMap);

        EXPORTED_METRIC = new ExportedMetric.Builder()
                .withName(GC_PS_SCAVENGE_COUNT)
                .withValue(1D)
                .withTimestamp(Date.from(Instant.EPOCH.plusSeconds(10)).getTime() / 1000L)
                .build();
    }

    @Test
    public void shouldValidateProcessor() {
        // When
        boolean validated = subject.validate(EXPORTED_METRIC);

        // Then
        assertTrue(validated);
    }

    @Test
    public void shouldNotValidateProcessorIfInvalidMetric() {
        // Given
        ExportedMetric exportedMetric = new ExportedMetric.Builder()
                .withName(INVALID)
                .build();

        // When
        boolean validated = subject.validate(exportedMetric);

        // Then
        assertFalse(validated);
    }

    @Test
    public void shouldReturnProperProcessor() {
        // When
        ProcessedMetric processedMetric = subject.process(EXPORTED_METRIC);

        // Then
        assertEquals(SYSTEM_METRICS_PREFIX + ACCUMULATED_METRICS_PREFIX + "gc", processedMetric.getName());
        assertEquals(MetricType.COUNTER, processedMetric.getMetricType());
        assertEquals(Double.valueOf(1D), processedMetric.getValue());
        assertEquals(Instant.EPOCH.plusSeconds(10).getEpochSecond(), processedMetric.getTimestamp());
        assertEquals("ps_scavenge", processedMetric.getTags().get().getTagValue("name"));
        assertFalse(processedMetric.getAggregations().isPresent());
        assertFalse(processedMetric.getAggregationDetails().isPresent());
    }

}
