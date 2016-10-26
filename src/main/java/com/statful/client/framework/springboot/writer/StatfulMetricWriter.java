package com.statful.client.framework.springboot.writer;

import com.statful.client.framework.springboot.proxy.StatfulClientProxy;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.actuate.metrics.Metric;
import org.springframework.boot.actuate.metrics.writer.Delta;
import org.springframework.boot.actuate.metrics.writer.MetricWriter;

import javax.annotation.Resource;

/**
 * {@link MetricWriter} implementation for Statful.
 */
public class StatfulMetricWriter implements MetricWriter {

    private static final Log LOGGER = LogFactory.getLog(StatfulMetricWriter.class);

    @Resource
    StatfulClientProxy statfulClientProxy;

    public StatfulMetricWriter() {
        LOGGER.info("Initializing StatfulMetricWriter");
    }

    @Override
    public void increment(Delta<?> delta) {
        statfulClientProxy.ingestMetric(delta);
    }

    @Override
    public void reset(String s) {
        LOGGER.debug("MetricWriter reset method is not implemented");
    }

    @Override
    public void set(Metric<?> metric) {
        statfulClientProxy.ingestMetric(metric);
    }

    public void setStatfulClientProxy(StatfulClientProxy statfulClientProxy) {
        this.statfulClientProxy = statfulClientProxy;
    }
}


