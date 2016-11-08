package com.statful.client.framework.springboot.processor.processors.system;

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
 * Processor responsible for parsing exported classes metrics.
 *
 * Example:
 *   classes=6366
 *   classes.loaded=6367
 *   classes.unloaded=1
 */
@Component
@ConditionalOnProperty(name = "statful.client.springboot.processors.system.classes.enabled",
        havingValue = "true", matchIfMissing = true)
public class ClassesProcessor implements MetricProcessor {

    private static final Tags TOTAL_TYPE_TAGS = Tags.from("type", "total");

    @Override
    public ProcessedMetric process(ExportedMetric exportedMetric) {
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
                .withValue(exportedMetric.getValue())
                .withTimestamp(exportedMetric.getTimestamp())
                .build();
    }

    @Override
    public List<String> getProcessorKeys() {
        return Collections.singletonList("classes");
    }
}
