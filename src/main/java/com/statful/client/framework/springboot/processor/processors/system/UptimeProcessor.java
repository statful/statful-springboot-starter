package com.statful.client.framework.springboot.processor.processors.system;

import com.statful.client.domain.api.Tags;
import com.statful.client.framework.springboot.common.ExportedMetric;
import com.statful.client.framework.springboot.common.MetricType;
import com.statful.client.framework.springboot.common.ProcessedMetric;
import com.statful.client.framework.springboot.processor.MetricProcessor;

public class UptimeProcessor implements MetricProcessor {

    private static final String UPTIME_NAME = SYSTEM_METRICS_PREFIX + "uptime";
    private static final Tags VM_TYPE_TAGS = Tags.from("type", "vm");

    @Override
    public ProcessedMetric process(ExportedMetric exportedMetric) {
        /**
         * Example:
         *  instance.uptime=442414
         *  uptime=44278
         */
        String[] metricSplit = exportedMetric.getName().split("\\.");

        Tags tags;
        switch (metricSplit.length) {
            case 1:
                tags = VM_TYPE_TAGS;
                break;
            case 2:
                tags = Tags.from("type", metricSplit[0]);
                break;
            default:
                throw new IllegalArgumentException();
        }

        return new ProcessedMetric.Builder().withName(UPTIME_NAME)
                .withTags(tags)
                .withMetricType(MetricType.GAUGE)
                .withValue(exportedMetric.getValue().doubleValue())
                .withTimestamp(exportedMetric.getTimestamp().getTime())
                .build();
    }
}
