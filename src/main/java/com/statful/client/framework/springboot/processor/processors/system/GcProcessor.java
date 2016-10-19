package com.statful.client.framework.springboot.processor.processors.system;

import com.statful.client.domain.api.Tags;
import com.statful.client.framework.springboot.common.ExportedMetric;
import com.statful.client.framework.springboot.common.MetricType;
import com.statful.client.framework.springboot.common.ProcessedMetric;
import com.statful.client.framework.springboot.processor.MetricProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

public class GcProcessor implements MetricProcessor {

    @Value("${statful.client.springboot.metrics.units.enabled:false}")
    private boolean unitsEnabled;

    @Override
    public ProcessedMetric process(ExportedMetric exportedMetric) {
        /**
         * Example:
         *  gc.ps_scavenge.count=5
         *  gc.ps_scavenge.time=55
         */
        String[] metricSplit = exportedMetric.getName().split("\\.");

        Tags tags = Tags.from("name", metricSplit[1]);

        MetricType metricType;

        switch (metricSplit[2]) {
            case "count":
                metricType = MetricType.COUNTER;
                break;
            case "time":
                metricType = MetricType.TIMER;
                if (unitsEnabled) {
                    tags.putTag("unit", "ms");
                }
                break;
            default:
                throw new IllegalArgumentException();
        }

        return new ProcessedMetric.Builder().withName(SYSTEM_METRICS_PREFIX + ACCUMULATED_METRICS_PREFIX + metricSplit[0])
                .withTags(tags)
                .withMetricType(metricType)
                .withValue(exportedMetric.getValue().doubleValue())
                .withTimestamp(exportedMetric.getTimestamp().getTime())
                .build();
    }
}
