package com.statful.client.framework.springboot.processor.processors.system;

import com.statful.client.framework.springboot.common.ExportedMetric;
import com.statful.client.framework.springboot.common.MetricType;
import com.statful.client.framework.springboot.common.ProcessedMetric;
import com.statful.client.framework.springboot.processor.GenericProcessor;
import com.statful.client.framework.springboot.processor.MetricProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * Processor responsible for parsing exported processors metrics.
 *
 * Example:
 *  processors=8
 */
@Component
@ConditionalOnProperty(name = "statful.client.springboot.processors.system.processors.enabled",
        havingValue = "true", matchIfMissing = true)
public class ProcessorsProcessor implements MetricProcessor {

    @Override
    public ProcessedMetric process(ExportedMetric exportedMetric) {
        return GenericProcessor.process(SYSTEM_METRICS_PREFIX + exportedMetric.getName(),
                MetricType.GAUGE, exportedMetric.getValue().doubleValue(), exportedMetric.getTimestamp().getTime());
    }

    @Override
    public List<String> getProcessorKeys() {
        return Collections.singletonList("processors");
    }

}
