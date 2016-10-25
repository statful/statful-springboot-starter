package com.statful.client.framework.springboot.proxy;

import com.statful.client.core.StatfulFactory;
import com.statful.client.domain.api.*;
import com.statful.client.framework.springboot.common.AggregationDetails;
import com.statful.client.framework.springboot.common.MetricType;
import com.statful.client.framework.springboot.common.ProcessedMetric;
import com.statful.client.framework.springboot.config.SpringbootClientConfiguration;
import com.statful.client.framework.springboot.processor.StatfulMetricProcessor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.springframework.boot.actuate.metrics.Metric;
import org.springframework.boot.actuate.metrics.writer.Delta;

import java.util.Collections;

import static com.statful.client.framework.springboot.config.SpringbootClientConfiguration.Metrics;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class StatfulClientProxyTest {

    private static String PREFIX = "prefix";
    private static String NAMESPACE = "namespace";
    private static String METRIC_NAME = "gc.test.foo";
    private static String INGESTED_METRIC_NAME = PREFIX + ".counter.gc.test.foo";
    private static Metrics.Tags GLOBAL_TAGS = new Metrics.Tags();
    private static String CUSTOM_TAG_KEY = "customKey";
    private static String CUSTOM_TAG_VALUE = "customValue";
    private static Tags CUSTOM_TAGS = Tags.from(CUSTOM_TAG_KEY, CUSTOM_TAG_VALUE);

    @Mock
    private SpringbootClientConfiguration springbootClientConfiguration;

    @Mock
    private StatfulMetricProcessor statfulMetricProcessor;

    @Mock
    private StatfulClient statfulClient = StatfulFactory.buildHTTPClient().with().isDryRun(true).build();

    private StatfulClientProxy subject;

    @Before
    public void before() {
        initMocks(this);

        when(springbootClientConfiguration.getMetrics()).thenReturn(buildDefaultMetrics());

        subject = new StatfulClientProxy();
        subject.setSpringbootClientConfiguration(springbootClientConfiguration);
        subject.setStatfulMetricProcessor(statfulMetricProcessor);
        subject.setStatfulClient(statfulClient);
        subject.setMetricsPrefix(PREFIX);
        subject.setMetricsNamespace(NAMESPACE);
    }

    private Metrics buildDefaultMetrics() {
        GLOBAL_TAGS.setName("globalKey");
        GLOBAL_TAGS.setValue("globalValue");

        Metrics metrics = new Metrics();
        metrics.setTags(Collections.singletonList(GLOBAL_TAGS));

        return metrics;
    }

    @Test
    public void shouldIngestFromDelta() {
        // Given
        when(statfulMetricProcessor.validate(any())).thenReturn(true);
        ProcessedMetric processedMetric = getProcessedMetric();
        when(statfulMetricProcessor.process(any())).thenReturn(processedMetric);
        Delta delta = new Delta<>(METRIC_NAME, 1L);

        // When
        subject.ingestMetric(delta);

        // Then
        verify(statfulClient).put(eq(INGESTED_METRIC_NAME), eq(Double.toString(1L)), any(Tags.class), eq(null),
                eq(null), anyInt(), eq(NAMESPACE), anyInt());
    }

    @Test
    public void shouldIngestFromMetric() {
        // Given
        when(statfulMetricProcessor.validate(any())).thenReturn(true);
        ProcessedMetric processedMetric = getProcessedMetric();
        when(statfulMetricProcessor.process(any())).thenReturn(processedMetric);
        Metric metric = new Metric<>(METRIC_NAME, 1L);

        // When
        subject.ingestMetric(metric);

        // Then
        verify(statfulClient).put(eq(INGESTED_METRIC_NAME), eq(Double.toString(1L)), any(Tags.class), eq(null),
                eq(null), anyInt(), eq(NAMESPACE), anyInt());
    }

    @Test
    public void shouldIngestFromAggregatedMetric() {
        // Given
        when(statfulMetricProcessor.validate(any())).thenReturn(true);
        ProcessedMetric processedMetric = getProcessedAggregatedMetric();
        when(statfulMetricProcessor.process(any())).thenReturn(processedMetric);
        Metric metric = new Metric<>(METRIC_NAME, 1L);

        // When
        subject.ingestMetric(metric);

        // Then
        verify(statfulClient).aggregatedPut(eq(INGESTED_METRIC_NAME), eq(Double.toString(1L)), any(Tags.class),
                any(Aggregation.class), any(AggregationFrequency.class), anyInt(), eq(NAMESPACE), anyInt());
    }

    @Test
    public void shouldIngestMetricWithDefaultGlobalTags() {
        // Given
        when(statfulMetricProcessor.validate(any())).thenReturn(true);
        ProcessedMetric processedMetric = getProcessedMetric();
        when(statfulMetricProcessor.process(any())).thenReturn(processedMetric);
        Metric metric = new Metric<>(METRIC_NAME, 1L);

        // When
        subject.ingestMetric(metric);

        // Then
        ArgumentCaptor<Tags> tags = ArgumentCaptor.forClass(Tags.class);
        verify(statfulClient).put(eq(INGESTED_METRIC_NAME), eq(Double.toString(1L)), tags.capture(), eq(null), eq(null),
                anyInt(), eq(NAMESPACE), anyInt());
        assertEquals(GLOBAL_TAGS.getValue(), tags.getValue().getTagValue(GLOBAL_TAGS.getName()));
    }

    @Test
    public void shouldIngestMetricWithMergedTags() {
        // Given
        when(statfulMetricProcessor.validate(any())).thenReturn(true);
        ProcessedMetric processedMetric = getProcessedMetricWithTags();
        when(statfulMetricProcessor.process(any())).thenReturn(processedMetric);
        Metric metric = new Metric<>(METRIC_NAME, 1L);

        // When
        subject.ingestMetric(metric);

        // Then
        ArgumentCaptor<Tags> tags = ArgumentCaptor.forClass(Tags.class);
        verify(statfulClient).put(eq(INGESTED_METRIC_NAME), eq(Double.toString(1L)), tags.capture(), eq(null), eq(null),
                anyInt(), eq(NAMESPACE), anyInt());
        assertEquals(GLOBAL_TAGS.getValue(), tags.getValue().getTagValue(GLOBAL_TAGS.getName()));
        assertEquals(CUSTOM_TAG_VALUE, tags.getValue().getTagValue(CUSTOM_TAG_KEY));
    }

    @Test
    public void shouldIngestMetricWithAggregations() {
        // Given
        when(statfulMetricProcessor.validate(any())).thenReturn(true);
        ProcessedMetric processedMetric = getProcessedMetricWithAggregations();
        when(statfulMetricProcessor.process(any())).thenReturn(processedMetric);
        Metric metric = new Metric<>(METRIC_NAME, 1L);

        // When
        subject.ingestMetric(metric);

        // Then
        ArgumentCaptor<Aggregations> aggregations = ArgumentCaptor.forClass(Aggregations.class);
        verify(statfulClient).put(eq(INGESTED_METRIC_NAME), eq(Double.toString(1L)), any(Tags.class),
                aggregations.capture(), eq(null), anyInt(), eq(NAMESPACE), anyInt());
        assertEquals(Aggregation.AVG, aggregations.getValue().getAggregations().stream().findFirst().get());
    }

    @Test
    public void shouldNotIngestUnknownMetricProcessor() {
        // Given
        when(statfulMetricProcessor.validate(any())).thenReturn(false);

        // When
        subject.ingestMetric(new Metric<Number>("unknown", 1L));

        // Then
        verifyNoMoreInteractions(statfulClient);
    }

    private ProcessedMetric getProcessedMetric() {
        return new ProcessedMetric.Builder()
                    .withName(METRIC_NAME)
                    .withMetricType(MetricType.COUNTER)
                    .withValue((double) 1L)
                    .build();
    }

    private ProcessedMetric getProcessedMetricWithTags() {
        return new ProcessedMetric.Builder()
                .withName(METRIC_NAME)
                .withMetricType(MetricType.COUNTER)
                .withTags(CUSTOM_TAGS)
                .withValue((double) 1L)
                .build();
    }

    private ProcessedMetric getProcessedMetricWithAggregations() {
        return new ProcessedMetric.Builder()
                .withName(METRIC_NAME)
                .withMetricType(MetricType.COUNTER)
                .withAggregations(Aggregations.from(Aggregation.AVG))
                .withValue((double) 1L)
                .build();
    }

    private ProcessedMetric getProcessedAggregatedMetric() {
        return new ProcessedMetric.Builder()
                .withName(METRIC_NAME)
                .withMetricType(MetricType.COUNTER)
                .withValue((double) 1L)
                .aggregatedBy(new AggregationDetails(Aggregation.AVG, AggregationFrequency.FREQ_10))
                .build();
    }

}
