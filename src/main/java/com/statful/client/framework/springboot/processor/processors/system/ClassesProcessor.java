package com.statful.client.framework.springboot.processor.processors.system;

import com.statful.client.domain.api.Tags;
import com.statful.client.framework.springboot.common.ExportedMetric;
import com.statful.client.framework.springboot.common.MetricType;
import com.statful.client.framework.springboot.common.ProcessedMetric;
import com.statful.client.framework.springboot.processor.MetricProcessor;

public class ClassesProcessor implements MetricProcessor {

    private static final Tags TOTAL_TYPE_TAGS = Tags.from("type", "total");

    @Override
    public ProcessedMetric process(ExportedMetric exportedMetric) {
        /**
         * Example:
         *   classes=6366
         *   classes.loaded=6367
         *   classes.unloaded=1
         */
        String[] metricSplit = exportedMetric.getName().split("\\.");

        Tags tags;

        switch (metricSplit.length) {
            case 1:
                tags = TOTAL_TYPE_TAGS;
                break;
            case 2:
                tags = Tags.from("type", metricSplit[1]);
                break;
            default:
                throw new IllegalArgumentException();
        }

        return new ProcessedMetric.Builder().withName(SYSTEM_METRICS_PREFIX + metricSplit[0])
                .withTags(tags)
                .withMetricType(MetricType.GAUGE)
                .withValue(exportedMetric.getValue().doubleValue())
                .withTimestamp(exportedMetric.getTimestamp().getTime())
                .build();
    }
}
