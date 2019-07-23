package com.statful.client.framework.springboot.registry;

import com.statful.client.core.StatfulFactory;
import com.statful.client.domain.api.SenderAPI;
import com.statful.client.domain.api.SenderFacade;
import com.statful.client.domain.api.StatfulClient;
import com.statful.client.domain.api.Tags;
import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.Meter;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.util.Assert;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class StatfulMetricRegistryTest {

    private final static String METRIC_NAME = "metric.name";
    private final static String TAG_KEY = "key";
    private final static String TAG_VALUE = "value";
    private final static String TIME_UNIT = TimeUnit.MILLISECONDS.name();
    private final static String DESCRIPTION = "description";

    @Mock
    private StatfulMetricsProperties statfulMetricsProperties;

    @Mock
    private StatfulMetricRegistryConfig statfulMetricRegistryConfig;

    @Mock
    private Clock clock = Clock.SYSTEM;

    @Mock
    private StatfulClient statfulClient;

    @Mock
    private SenderFacade senderFacade;

    @Mock
    private SenderAPI senderAPI;

    @InjectMocks
    private StatfulMetricRegistry statfulMetricRegistry;

    @Test
    public void shouldRegisterMetric() {
        initMocks(this);

        when(statfulMetricRegistryConfig.step()).thenReturn(Duration.ofSeconds(10));

        statfulMetricRegistry.counter(METRIC_NAME, TAG_KEY, TAG_VALUE);

        List<Meter> meters = statfulMetricRegistry.getMeters();

        assertFalse(meters.isEmpty());
        assertEquals(1, meters.size());
    }

    @Test
    public void shouldSendMetricWhenPublishedByRegistry() {
        initMocks(this);

        when(statfulMetricRegistryConfig.step()).thenReturn(Duration.ofSeconds(10));

        statfulMetricRegistry.counter(METRIC_NAME, TAG_KEY, TAG_VALUE);

        List<Meter> meters = statfulMetricRegistry.getMeters();

        Assert.notEmpty(meters, "Meters must not be empty");
        Assert.isTrue(meters.size() == 1, "There should only be one registered meter");

        when(statfulClient.counter(eq(METRIC_NAME), anyInt())).thenReturn(senderFacade);
        when(senderFacade.with()).thenReturn(senderAPI);
        when(senderAPI.tags(any(Tags.class))).thenReturn(senderAPI);

        statfulMetricRegistry.publish();

        Mockito.verify(senderAPI, times(1)).send();
    }

    @Test
    public void shouldAllowMetricsThatMatchNameRestrictions() {
        statfulClient = StatfulFactory.buildHTTPClient().with().isDryRun(true).build();

        statfulMetricsProperties = new StatfulMetricsProperties();
        statfulMetricRegistryConfig = new StatfulMetricRegistryConfig();

        statfulMetricsProperties.setAcceptedMetrics(Collections.singletonList("metric"));
        statfulMetricRegistryConfig.setStep(10);

        statfulMetricRegistry = new StatfulMetricRegistry(statfulMetricRegistryConfig, statfulClient, clock, statfulMetricsProperties);

        statfulMetricRegistry.counter(METRIC_NAME, TAG_KEY, TAG_VALUE);

        List<Meter> meters = statfulMetricRegistry.getMeters();

        assertFalse(meters.isEmpty());
        assertEquals(1, meters.size());
    }

    @Test
    public void shouldNotAllowMetricsThatDoNotMatchNameRestrictions() {
        statfulClient = StatfulFactory.buildHTTPClient().with().isDryRun(true).build();

        statfulMetricsProperties = new StatfulMetricsProperties();
        statfulMetricRegistryConfig = new StatfulMetricRegistryConfig();

        statfulMetricsProperties.setAcceptedMetrics(Collections.singletonList("invalid"));
        statfulMetricRegistryConfig.setStep(10);

        statfulMetricRegistry = new StatfulMetricRegistry(statfulMetricRegistryConfig, statfulClient, clock, statfulMetricsProperties);

        statfulMetricRegistry.counter(METRIC_NAME, TAG_KEY, TAG_VALUE);

        List<Meter> meters = statfulMetricRegistry.getMeters();

        assertTrue(meters.isEmpty());
    }

    @Test
    public void shouldReplaceNameOfMetricsThatMatchPrefix() {
        statfulClient = StatfulFactory.buildHTTPClient().with().isDryRun(true).build();

        statfulMetricsProperties = new StatfulMetricsProperties();
        statfulMetricRegistryConfig = new StatfulMetricRegistryConfig();

        statfulMetricsProperties.setAlias(Collections.singletonMap("metric", "newMetric"));
        statfulMetricRegistryConfig.setStep(10);

        statfulMetricRegistry = new StatfulMetricRegistry(statfulMetricRegistryConfig, statfulClient, clock, statfulMetricsProperties);

        statfulMetricRegistry.counter(METRIC_NAME, TAG_KEY, TAG_VALUE);

        List<Meter> meters = statfulMetricRegistry.getMeters();

        assertFalse(meters.isEmpty());
        assertEquals(1, meters.size());
        assertEquals("newMetric", meters.get(0).getId().getName());
    }

    @Test
    public void shouldAddTagsToMetricsThatMatchPrefix() {
        statfulClient = StatfulFactory.buildHTTPClient().with().isDryRun(true).build();

        statfulMetricsProperties = new StatfulMetricsProperties();
        statfulMetricRegistryConfig = new StatfulMetricRegistryConfig();

        statfulMetricsProperties.setTags(Collections.singletonMap("metric", "key=value"));
        statfulMetricRegistryConfig.setStep(10);

        statfulMetricRegistry = new StatfulMetricRegistry(statfulMetricRegistryConfig, statfulClient, clock, statfulMetricsProperties);

        statfulMetricRegistry.counter(METRIC_NAME, TAG_KEY, TAG_VALUE);

        List<Meter> meters = statfulMetricRegistry.getMeters();

        assertFalse(meters.isEmpty());
        assertEquals(1, meters.size());
        assertEquals("value", meters.get(0).getId().getTag("key"));
    }
}
