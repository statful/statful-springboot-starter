package com.statful.client.framework.springboot.registry;

import com.statful.client.domain.api.StatfulClient;
import com.statful.client.domain.api.Tags;
import io.micrometer.core.instrument.*;
import io.micrometer.core.instrument.config.MeterFilter;
import io.micrometer.core.instrument.distribution.HistogramSnapshot;
import io.micrometer.core.instrument.step.StepMeterRegistry;
import io.micrometer.core.instrument.step.StepRegistryConfig;
import org.apache.logging.log4j.util.Strings;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static io.micrometer.core.instrument.util.StringUtils.isNotEmpty;

public class StatfulMetricRegistry extends StepMeterRegistry {

    private StatfulClient statfulClient;

    private StatfulMetricsProperties properties;

    public StatfulMetricRegistry(StepRegistryConfig config, StatfulClient statfulClient, Clock clock, StatfulMetricsProperties properties) {
        super(config, clock);

        this.statfulClient = statfulClient;
        this.properties = properties;

        applyCustomFilter();

        start();
    }

    /**
     * Applies two filters, one that looks into the property "acceptedMetrics" and another that looks into the "alias"
     * and "tags" properties in {@link StatfulMetricsProperties}.
     * <p>
     * The first filter matches the prefixes defined in the list against the metrics. If the list is empty all metrics are
     * accepted. If an alias has been applied to the metric beforehand then the filter will also search for the
     * corresponding key in the "alias" property and accept the metric if found.
     * <p>
     * The second filter adds the tags to the metrics that match the keys in "tags" and replaces the name for the metrics
     * that match the keys in "alias"
     */
    private void applyCustomFilter() {
        this.config()
                .meterFilter(
                        MeterFilter.denyUnless(id ->
                                properties.getAcceptedMetrics().isEmpty() || properties.getAcceptedMetrics().stream()
                                        .anyMatch(metricName -> id.getName().startsWith(metricName) || isNotEmpty(getAliasForMetric(id)))))

                .meterFilter(new MeterFilter() {

                    @Override
                    public Meter.Id map(Meter.Id id) {
                        Meter.Id newId = id;

                        for (Map.Entry<String, String> entry : properties.getTags().entrySet()) {
                            if (id.getName().startsWith(entry.getKey())) {
                                List<Tag> tags = toListOfTags(entry.getValue());

                                newId = newId.withTags(tags);
                            }
                        }

                        String metricAlias = getAliasForMetric(newId);

                        if (isNotEmpty(metricAlias)) {
                            newId = newId.withName(metricAlias);
                        }

                        return newId;
                    }
                });

    }

    private String getAliasForMetric(Meter.Id id) {
        for (Map.Entry<String, String> entry : properties.getAlias().entrySet()) {
            if (id.getName().startsWith(entry.getKey())) {
                return entry.getValue();
            }
        }
        return Strings.EMPTY;
    }

    private List<Tag> toListOfTags(String tags) {
        String[] tagsAsList = tags.split(";");

        return Arrays.stream(tagsAsList)
                .map(tag -> {
                    String[] splitTag = tag.split("=");

                    return new Tag() {
                        @Override
                        public String getKey() {
                            return splitTag[0];
                        }

                        @Override
                        public String getValue() {
                            return splitTag[1];
                        }
                    };
                })
                .collect(Collectors.toList());
    }

    protected void publish() {
        this.getMeters().forEach((meter) -> {
            if (meter instanceof Timer) {
                this.writeTimer((Timer) meter);
            } else if (!(meter instanceof FunctionTimer) && !(meter instanceof DistributionSummary) && !(meter instanceof TimeGauge)) {
                if (meter instanceof Gauge) {
                    this.writeGauge((Gauge) meter);
                } else if (meter instanceof Counter) {
                    this.writeCounter((Counter) meter);
                } else if (meter instanceof FunctionCounter) {
                    this.writeCounter((FunctionCounter) meter);
                } else if (meter instanceof LongTaskTimer) {
                }
            }

        });
    }

    protected void writeCounter(FunctionCounter counter) {
        Meter.Id meterId = counter.getId();

        com.statful.client.domain.api.Tags tags = new com.statful.client.domain.api.Tags();
        meterId.getTags().forEach((tag) -> tags.putTag(tag.getKey(), tag.getValue()));

        this.statfulClient.counter(meterId.getName(), Double.valueOf(counter.count()).intValue()).with().tags(tags).send();
    }

    protected void writeCounter(Counter counter) {
        Meter.Id meterId = counter.getId();

        com.statful.client.domain.api.Tags tags = new com.statful.client.domain.api.Tags();

        meterId.getTags().forEach((tag) -> tags.putTag(tag.getKey(), tag.getValue()));

        this.statfulClient.counter(meterId.getName(), (new Double(counter.count())).intValue()).with().tags(tags).send();
    }

    protected void writeGauge(Gauge gauge) {
        Meter.Id meterId = gauge.getId();
        com.statful.client.domain.api.Tags tags = new com.statful.client.domain.api.Tags();

        meterId.getTags().forEach((tag) -> tags.putTag(tag.getKey(), tag.getValue()));

        this.statfulClient.gauge(meterId.getName(), gauge.value()).with().tags(tags).send();
    }

    protected void writeTimer(Timer timer) {
        HistogramSnapshot histogramSnapshot = timer.takeSnapshot();
        Meter.Id meterId = timer.getId();
        com.statful.client.domain.api.Tags tags = new Tags();

        meterId.getTags().forEach((tag) -> tags.putTag(tag.getKey(), tag.getValue()));

        long value = (new Double(histogramSnapshot.mean(this.getBaseTimeUnit()))).longValue();

        this.statfulClient.timer(meterId.getName(), value).with().tags(tags).send();
    }

    protected void writeLongTaskTimer(LongTaskTimer ltt) {
    }

    protected void writeGauge(TimeGauge gauge) {
    }

    protected void writeSummary(DistributionSummary summary) {
    }

    protected void writeTimer(FunctionTimer timer) {
    }

    protected void writeMeter(Meter meter) {
    }


    @Override
    protected TimeUnit getBaseTimeUnit() {
        return TimeUnit.MILLISECONDS;
    }
}
