package com.statful.client.framework.springboot.processor.processors.system;

import com.statful.client.domain.api.Tags;
import com.statful.client.framework.springboot.common.ExportedMetric;
import com.statful.client.framework.springboot.common.MetricType;
import com.statful.client.framework.springboot.common.ProcessedMetric;
import com.statful.client.framework.springboot.processor.MetricProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Processor responsible for parsing exported gc metrics and calculating the variation between
 * <p>
 * Example:
 * gc.ps_scavenge.count=5
 * gc.ps_scavenge.time=55
 */
@Component
@ConditionalOnProperty(name = "statful.client.springboot.processors.system.gc.diff.enabled",
        havingValue = "true", matchIfMissing = true)
public class GcDiffProcessor implements MetricProcessor {

    private Map<String, Double> counterStorage = new HashMap<>();

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public ProcessedMetric process(ExportedMetric exportedMetric) {
        String[] metricSplit = exportedMetric.getName().split("\\.");

        if (metricSplit.length != 3) {
            throw new IllegalArgumentException();
        }

        Tags tags = Tags.from("name", metricSplit[1]);

        MetricType metricType = MetricType.fromName(metricSplit[2]);

        // store the current value and calculate the difference from the previous value.
        Double previousValue = counterStorage.put(exportedMetric.getName(), exportedMetric.getValue());
        Double value = previousValue == null ? exportedMetric.getValue() : Math.abs(exportedMetric.getValue() - previousValue);

        return new ProcessedMetric.Builder().withName(SYSTEM_METRICS_PREFIX + metricSplit[0])
                .withTags(tags)
                .withMetricType(metricType)
                .withValue(value)
                .withTimestamp(exportedMetric.getTimestamp())
                .build();
    }

    @Override
    public List<String> getProcessorKeys() {
        return Collections.singletonList("gc");
    }
}
