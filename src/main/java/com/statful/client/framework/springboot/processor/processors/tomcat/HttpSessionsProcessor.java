package com.statful.client.framework.springboot.processor.processors.tomcat;

import com.statful.client.domain.api.Tags;
import com.statful.client.framework.springboot.common.ExportedMetric;
import com.statful.client.framework.springboot.common.MetricType;
import com.statful.client.framework.springboot.common.ProcessedMetric;
import com.statful.client.framework.springboot.processor.MetricProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * Processor responsible for parsing exported tomcat http sessions metrics.
 *
 * Example:
 *  httpsessions.max=-1
 *  httpsessions.active=5
 */
@Component
@ConditionalOnProperty(name = "statful.client.springboot.processors.tomcat.httpSessions.enabled",
        havingValue = "true", matchIfMissing = true)
public class HttpSessionsProcessor implements MetricProcessor {

    @Override
    public ProcessedMetric process(ExportedMetric exportedMetric) {
        String[] metricSplit = exportedMetric.getName().split("\\.");

        if (metricSplit.length != 2) {
            throw new IllegalArgumentException();
        }

        Tags tags = Tags.from("type", metricSplit[1]);

        return new ProcessedMetric.Builder().withName(TOMCAT_METRICS_PREFIX + metricSplit[0])
                .withTags(tags)
                .withMetricType(MetricType.GAUGE)
                .withValue(exportedMetric.getValue())
                .withTimestamp(exportedMetric.getTimestamp())
                .build();
    }

    @Override
    public List<String> getProcessorKeys() {
        return Collections.singletonList("httpsessions");
    }
}
