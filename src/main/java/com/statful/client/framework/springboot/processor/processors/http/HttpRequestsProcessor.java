package com.statful.client.framework.springboot.processor.processors.http;

import com.statful.client.domain.api.Tags;
import com.statful.client.framework.springboot.common.ExportedMetric;
import com.statful.client.framework.springboot.common.MetricType;
import com.statful.client.framework.springboot.common.ProcessedMetric;
import com.statful.client.framework.springboot.processor.MetricProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@ConditionalOnProperty(name = "statful.client.springboot.processors.http.httpRequests.enabled",
        havingValue = "true", matchIfMissing = true)
public class HttpRequestsProcessor implements MetricProcessor {

    private static final String REQUESTS_NAME = ACCUMULATED_METRICS_PREFIX + "requests";
    private static final String RESPONSES_NAME = LATEST_METRICS_PREFIX + "responses";
    private static Pattern COUNTER_STATUS_PATTERN = Pattern.compile("^counter\\.status\\.(\\d{3})\\.(.+)$");
    private static Pattern GAUGE_RESPONSE_PATTERN = Pattern.compile("^gauge\\.response\\.(.+)$");

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

    @Override
    public List<String> getProcessorKeys() {
        List<String> keys = new LinkedList<>();
        keys.add("counter.status");
        keys.add("gauge.response");

        return keys;
    }

    private ProcessedMetric processCounter(String status, String url, ExportedMetric exportedMetric) {
        Tags tags = new Tags();
        tags.putTag("status", status);
        tags.putTag("url", url);

        return new ProcessedMetric.Builder().withName(REQUESTS_NAME)
                .withTags(tags)
                .withMetricType(MetricType.COUNTER)
                .withValue(exportedMetric.getValue().doubleValue())
                .withTimestamp(exportedMetric.getTimestamp().getTime())
                .build();
    }

    private ProcessedMetric processGauge(String url, ExportedMetric exportedMetric) {
        Tags tags = new Tags();
        tags.putTag("url", url);

        return new ProcessedMetric.Builder().withName(RESPONSES_NAME)
                .withTags(tags)
                .withMetricType(MetricType.TIMER)
                .withValue(exportedMetric.getValue().doubleValue())
                .withTimestamp(exportedMetric.getTimestamp().getTime())
                .build();
    }
}
