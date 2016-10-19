package com.statful.client.framework.springboot.processor.processors.tomcat;

import com.statful.client.domain.api.Tags;
import com.statful.client.framework.springboot.common.ExportedMetric;
import com.statful.client.framework.springboot.common.MetricType;
import com.statful.client.framework.springboot.common.ProcessedMetric;
import com.statful.client.framework.springboot.processor.MetricProcessor;

public class HttpSessionsProcessor implements MetricProcessor {

    @Override
    public ProcessedMetric process(ExportedMetric exportedMetric) {
        /**
         * Example:
         *  httpsessions.max=-1
         *  httpsessions.active=5
         */
        String[] metricSplit = exportedMetric.getName().split("\\.");

        Tags tags = Tags.from("type", metricSplit[1]);

        return new ProcessedMetric.Builder().withName(TOMCAT_METRICS_PREFIX + metricSplit[0])
                .withTags(tags)
                .withMetricType(MetricType.GAUGE)
                .withValue(exportedMetric.getValue().doubleValue())
                .withTimestamp(exportedMetric.getTimestamp().getTime())
                .build();
    }
}
