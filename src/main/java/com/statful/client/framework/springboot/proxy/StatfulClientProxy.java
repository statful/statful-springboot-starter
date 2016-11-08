package com.statful.client.framework.springboot.proxy;

import com.statful.client.domain.api.Aggregations;
import com.statful.client.domain.api.StatfulClient;
import com.statful.client.domain.api.Tags;
import com.statful.client.framework.springboot.common.ExportedMetric;
import com.statful.client.framework.springboot.common.MetricType;
import com.statful.client.framework.springboot.common.ProcessedMetric;
import com.statful.client.framework.springboot.config.SpringbootClientConfiguration;
import com.statful.client.framework.springboot.processor.StatfulMetricProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.metrics.Metric;
import org.springframework.boot.actuate.metrics.writer.Delta;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * Class to proxy metrics into a {@link StatfulClient} implementation on the springboot client context.
 */
@Component
@ConditionalOnBean(value = StatfulClient.class)
public class StatfulClientProxy {

    @Resource
    private SpringbootClientConfiguration springbootClientConfiguration;

    @Resource
    private StatfulMetricProcessor statfulMetricProcessor;

    @Resource
    private StatfulClient statfulClient;

    @Value("${statful.client.springboot.metrics.prefix:springboot}")
    private String metricsPrefix;

    @Value("${statful.client.springboot.metrics.namespace:#{null}}")
    private String metricsNamespace;

    /**
     * Ingest a raw {@link Metric}.
     *
     * @param metric Raw {@link Metric} exported by springboot
     */
    public void ingestMetric(Metric<?> metric) {
        ExportedMetric exportedMetric = new ExportedMetric.Builder()
                .withName(metric.getName())
                .withValue(metric.getValue().doubleValue())
                .withTimestamp(metric.getTimestamp().getTime() / 1000L)
                .build();

        ingest(exportedMetric);
    }

    /**
     * Ingest a raw {@link Delta}.
     *
     * @param delta Raw {@link Delta} exported by springboot
     */
    public void ingestMetric(Delta<?> delta) {
        ExportedMetric exportedMetric = new ExportedMetric.Builder()
                .withName(delta.getName())
                .withValue(delta.getValue().doubleValue())
                .withTimestamp(delta.getTimestamp().getTime() / 1000L)
                .build();

        ingest(exportedMetric);
    }

    private void ingest(ExportedMetric exportedMetric) {
        if (statfulMetricProcessor.validate(exportedMetric)) {
            ProcessedMetric processedMetric = statfulMetricProcessor.process(exportedMetric);

            if (processedMetric.getAggregationDetails().isPresent()) {
                putMetricAggregated(processedMetric);
            } else {
                putMetric(processedMetric);
            }
        }
    }

    private void putMetricAggregated(ProcessedMetric processedMetric) {
        statfulClient.aggregatedPut(buildMetricName(processedMetric.getMetricType(), processedMetric.getName()),
                processedMetric.getValue().toString(), mergeDefaultTags(processedMetric.getTags()),
                processedMetric.getAggregationDetails().get().getAggregation(),
                processedMetric.getAggregationDetails().get().getAggregationFrequency(), 100, metricsNamespace,
                processedMetric.getTimestamp());
    }

    private void putMetric(ProcessedMetric processedMetric) {
        statfulClient.put(buildMetricName(processedMetric.getMetricType(), processedMetric.getName()),
                processedMetric.getValue().toString(), mergeDefaultTags(processedMetric.getTags()),
                getOrDefaultAggregations(processedMetric.getAggregations()), null, 100, metricsNamespace,
                processedMetric.getTimestamp());
    }

    private Tags mergeDefaultTags(Optional<Tags> tags) {
        Tags mergedTags = new Tags();
        if (tags.isPresent()) {
            mergedTags = tags.get();
        }

        Tags customTags = new Tags();
        springbootClientConfiguration.getMetrics().getTags().forEach(tag ->
                customTags.putTag(tag.getName(), tag.getValue()));

        return mergedTags.merge(customTags);
    }

    private Aggregations getOrDefaultAggregations(Optional<Aggregations> aggregations) {
        return aggregations.isPresent() ? aggregations.get() : null;
    }

    private String buildMetricName(MetricType metricType, String metricName) {
        return metricsPrefix.replaceAll("\\.$", "") + "." + metricType.name().toLowerCase() + "." + metricName;
    }

    public void setSpringbootClientConfiguration(SpringbootClientConfiguration springbootClientConfiguration) {
        this.springbootClientConfiguration = springbootClientConfiguration;
    }

    public void setStatfulMetricProcessor(StatfulMetricProcessor statfulMetricProcessor) {
        this.statfulMetricProcessor = statfulMetricProcessor;
    }

    public void setStatfulClient(StatfulClient statfulClient) {
        this.statfulClient = statfulClient;
    }

    public void setMetricsPrefix(String metricsPrefix) {
        this.metricsPrefix = metricsPrefix;
    }

    public void setMetricsNamespace(String metricsNamespace) {
        this.metricsNamespace = metricsNamespace;
    }
}
