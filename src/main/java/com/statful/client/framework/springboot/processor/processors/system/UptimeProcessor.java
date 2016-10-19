package com.statful.client.framework.springboot.processor.processors.system;

import com.statful.client.domain.api.Tags;
import com.statful.client.framework.springboot.common.ExportedMetric;
import com.statful.client.framework.springboot.common.MetricType;
import com.statful.client.framework.springboot.common.ProcessedMetric;
import com.statful.client.framework.springboot.processor.MetricProcessor;
import org.springframework.beans.factory.annotation.Value;

public class UptimeProcessor implements MetricProcessor {

    @Value("${statful.client.springboot.metrics.units.enabled:false}")
    private boolean unitsEnabled;

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
                tags = Tags.from("type", "vm");
                break;
            case 2:
                tags = Tags.from("type", metricSplit[0]);
                break;
            default:
                throw new IllegalArgumentException();
        }

        if (unitsEnabled) {
            tags.putTag("unit", "ms");
        }

        return new ProcessedMetric.Builder().withName(SYSTEM_METRICS_PREFIX + "uptime")
                .withTags(tags)
                .withMetricType(MetricType.GAUGE)
                .withValue(exportedMetric.getValue().doubleValue())
                .withTimestamp(exportedMetric.getTimestamp().getTime())
                .build();
    }
}
