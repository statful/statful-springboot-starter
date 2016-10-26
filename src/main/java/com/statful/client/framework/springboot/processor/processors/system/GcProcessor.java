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

@Component
@ConditionalOnProperty(name = "statful.client.springboot.processors.system.gc.enabled",
        havingValue = "true", matchIfMissing = true)
public class GcProcessor implements MetricProcessor {

    @Override
    public ProcessedMetric process(ExportedMetric exportedMetric) {
        /**
         * Example:
         *  gc.ps_scavenge.count=5
         *  gc.ps_scavenge.time=55
         */
        String[] metricSplit = exportedMetric.getName().split("\\.");

        if (metricSplit.length != 3) {
            throw  new IllegalArgumentException();
        }

        Tags tags = Tags.from("name", metricSplit[1]);

        MetricType metricType;

        switch (metricSplit[2]) {
            case "count":
                metricType = MetricType.COUNTER;
                break;
            case "time":
                metricType = MetricType.TIMER;
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

    @Override
    public List<String> getProcessorKeys() {
        return Collections.singletonList("gc");
    }
}
