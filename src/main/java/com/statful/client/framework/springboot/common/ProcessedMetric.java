package com.statful.client.framework.springboot.common;

import com.statful.client.domain.api.Aggregation;
import com.statful.client.domain.api.AggregationFreq;
import com.statful.client.domain.api.Aggregations;
import com.statful.client.domain.api.Tags;

import java.util.Objects;
import java.util.Optional;

public class ProcessedMetric {

    private String name;
    private MetricType metricType;
    private Tags tags;
    private Double value;
    private long timestamp;
    private Aggregations aggregations;
    private AggregationDetails aggregationDetails;

    public ProcessedMetric(Builder builder) {
        this.name = builder.name;
        this.metricType = builder.metricType;
        this.tags = builder.tags;
        this.value = builder.value;
        this.timestamp = builder.timestamp;
        this.aggregations = builder.aggregations;
        this.aggregationDetails = builder.aggregationDetails;
    }

    public String getName() {
        return name;
    }

    public MetricType getMetricType() {
        return metricType;
    }

    public Optional<Tags> getTags() {
        return Optional.ofNullable(tags);
    }

    public Double getValue() {
        return value;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Optional<Aggregations> getAggregations() {
        return Optional.ofNullable(aggregations);
    }

    public Optional<AggregationDetails> getAggregationDetails() {
        return Optional.ofNullable(aggregationDetails);
    }

    public static final class Builder {

        private String name;
        private MetricType metricType;
        private Tags tags;
        private Double value;
        private long timestamp;
        private Aggregations aggregations;
        private AggregationDetails aggregationDetails;

        public Builder withName(String name) {
            this.name = Objects.requireNonNull(name, "Metric name cannot be null");
            return this;
        }

        public Builder withMetricType(MetricType metricType) {
            this.metricType = metricType;
            return this;
        }

        public Builder withTags(Tags tags) {
            this.tags = tags;
            return this;
        }

        public Builder withValue(Double value) {
            this.value = Objects.requireNonNull(value, "Value cannot be null");
            return this;
        }

        public Builder withTimestamp(long timestamp) {
            this.timestamp = Objects.requireNonNull(timestamp, "Timestamp cannot be null");
            return this;
        }

        public Builder withAggregations(Aggregations aggregations) {
            this.aggregations = aggregations;
            return this;
        }

        public Builder aggregatedBy(AggregationDetails aggregationDetails) {
            this.aggregationDetails = aggregationDetails;
            return this;
        }

        public ProcessedMetric build() {
            return new ProcessedMetric(this);
        }

    }
}
