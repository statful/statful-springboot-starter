package com.statful.client.framework.springboot.processor.processors.system;

import com.statful.client.domain.api.Aggregation;
import com.statful.client.domain.api.AggregationFrequency;
import com.statful.client.framework.springboot.common.AggregationDetails;
import com.statful.client.framework.springboot.common.ExportedMetric;
import com.statful.client.framework.springboot.common.MetricType;
import com.statful.client.framework.springboot.common.ProcessedMetric;
import com.statful.client.framework.springboot.processor.MetricProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@ConditionalOnProperty(name = "statful.client.springboot.processors.system.systemload.enabled",
        havingValue = "true", matchIfMissing = true)
public class SystemLoadProcessor implements MetricProcessor {

    @Override
    public ProcessedMetric process(ExportedMetric exportedMetric) {
        // Example: systemload.average=1.11765
        String[] metricSplit = exportedMetric.getName().split("\\.");

        if (metricSplit.length != 2) {
            throw new IllegalArgumentException();
        }

        return new ProcessedMetric.Builder().withName(SYSTEM_METRICS_PREFIX + metricSplit[0])
                .withMetricType(MetricType.GAUGE)
                .withValue(exportedMetric.getValue().doubleValue())
                .withTimestamp(exportedMetric.getTimestamp().getTime())
                .aggregatedBy(new AggregationDetails(Aggregation.AVG, AggregationFrequency.FREQ_60))
                .build();
    }

    @Override
    public List<String> getProcessorKeys() {
        return Collections.singletonList("systemload");
    }
}
