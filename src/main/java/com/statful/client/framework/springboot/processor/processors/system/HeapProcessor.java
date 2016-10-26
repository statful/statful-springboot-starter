package com.statful.client.framework.springboot.processor.processors.system;

import com.statful.client.domain.api.Tags;
import com.statful.client.framework.springboot.common.ExportedMetric;
import com.statful.client.framework.springboot.common.MetricType;
import com.statful.client.framework.springboot.common.ProcessedMetric;
import com.statful.client.framework.springboot.processor.MetricProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
@ConditionalOnProperty(name = "statful.client.springboot.processors.system.heap.enabled",
        havingValue = "true", matchIfMissing = true)
public class HeapProcessor implements MetricProcessor {

    private static final Tags MAX_TYPE_TAGS = Tags.from("type", "max");

    @Override
    public ProcessedMetric process(ExportedMetric exportedMetric) {
        /**
         * Example:
         *  heap.committed=389632
         *  heap.init=262144
         *  heap.used=131031
         *  heap=3728384
         */
        String[] metricSplit = exportedMetric.getName().split("\\.");

        Tags tags;

        switch (metricSplit.length) {
            case 1:
                tags = MAX_TYPE_TAGS;
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

    @Override
    public List<String> getProcessorKeys() {
        List<String> keys = new LinkedList<>();
        keys.add("heap");
        keys.add("nonheap");
        return keys;
    }
}
