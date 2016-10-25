package com.statful.client.framework.springboot.writer;

import com.statful.client.framework.springboot.proxy.StatfulClientProxy;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.boot.actuate.metrics.Metric;
import org.springframework.boot.actuate.metrics.writer.Delta;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.MockitoAnnotations.initMocks;

public class StatfulMetricWriterTest {

    private StatfulMetricWriter subject;

    @Mock
    private StatfulClientProxy statfulClientProxy;

    @Before
    public void before() {
        initMocks(this);

        subject = new StatfulMetricWriter();
        subject.setStatfulClientProxy(statfulClientProxy);
    }

    @Test
    public void shouldIngestMetricOnIncrement() {
        // When
        Delta delta = new Delta<>("foo", 1L);
        subject.increment(delta);

        // Then
        verify(statfulClientProxy).ingestMetric(delta);
    }

    @Test
    public void shouldIngestMetricOnSet() {
        // When
        Metric metric = new Metric<>("bar", 2L);
        subject.set(metric);

        // Then
        verify(statfulClientProxy).ingestMetric(metric);
    }

    @Test
    public void shouldStubOnReset() {
        // When
        subject.reset("zed");

        // Then
        verifyNoMoreInteractions(statfulClientProxy);
    }
}
