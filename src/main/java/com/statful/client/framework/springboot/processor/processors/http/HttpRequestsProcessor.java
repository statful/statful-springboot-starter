package com.statful.client.framework.springboot.processor.processors.http;

import com.statful.client.domain.api.Tags;
import com.statful.client.framework.springboot.common.ExportedMetric;
import com.statful.client.framework.springboot.common.MetricType;
import com.statful.client.framework.springboot.common.ProcessedMetric;
import com.statful.client.framework.springboot.processor.MetricProcessor;
import org.springframework.beans.factory.annotation.Value;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpRequestsProcessor implements MetricProcessor {

    private static Pattern COUNTER_STATUS_PATTERN = Pattern.compile("^counter\\.status\\.(\\d{3})\\.(.+)$");
    private static Pattern GAUGE_RESPONSE_PATTERN = Pattern.compile("^gauge\\.response\\.(.+)$");

    @Value("${statful.client.springboot.metrics.units.enabled.enabled:false}")
    private boolean unitsEnabled;

    @Override
    public ProcessedMetric process(ExportedMetric exportedMetric) {
        /**
         * Example:
         *  counter.status.200.root=20
         *  counter.status.200.metrics=3
         *  counter.status.200.star-star.favicon.ico=5
         *  counter.status.401.root=4
         *  gauge.response.star-star.favicon.ico=6
         *  gauge.response.root=2
         *  gauge.response.metrics=3
         */

        Matcher counterMatcher = COUNTER_STATUS_PATTERN.matcher(exportedMetric.getName());
        Matcher gaugeMatcher = GAUGE_RESPONSE_PATTERN.matcher(exportedMetric.getName());

        if (counterMatcher.matches()) {
            return processCounter(counterMatcher.group(1), counterMatcher.group(2), exportedMetric);
        } else if (gaugeMatcher.matches()) {
            return processGauge(gaugeMatcher.group(1), exportedMetric);
        } else {
            throw new IllegalArgumentException();
        }
    }

    private ProcessedMetric processCounter(String status, String url, ExportedMetric exportedMetric) {
        Tags tags = new Tags();
        tags.putTag("status", status);
        tags.putTag("url", url);

        return new ProcessedMetric.Builder().withName(ACCUMULATED_METRICS_PREFIX + "requests")
                .withTags(tags)
                .withMetricType(MetricType.COUNTER)
                .withValue(exportedMetric.getValue().doubleValue())
                .withTimestamp(exportedMetric.getTimestamp().getTime())
                .build();
    }

    private ProcessedMetric processGauge(String url, ExportedMetric exportedMetric) {
        Tags tags = new Tags();
        tags.putTag("url", url);

        if (unitsEnabled) {
            tags.putTag("unit", "ms");
        }

        return new ProcessedMetric.Builder().withName(LATEST_METRICS_PREFIX + "responses")
                .withTags(tags)
                .withMetricType(MetricType.TIMER)
                .withValue(exportedMetric.getValue().doubleValue())
                .withTimestamp(exportedMetric.getTimestamp().getTime())
                .build();
    }
}
