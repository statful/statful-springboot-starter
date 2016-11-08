package com.statful.client.framework.springboot.common;

import java.util.Objects;

/**
 * Class to represent a metric exported by Actuator.
 */
public class ExportedMetric {
    private String name;
    private double value;
    private long timestamp;

    /**
     * Builder constructor.
     * @param builder {@link com.statful.client.framework.springboot.common.ExportedMetric.Builder}
     */
    public ExportedMetric(Builder builder) {
        this.name = builder.name;
        this.value = builder.value;
        this.timestamp = builder.timestamp;
    }

    public String getName() {
        return name;
    }

    public double getValue() {
        return value;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public static final class Builder {

        private String name;
        private double value;
        private long timestamp;

        public Builder withName(String name) {
            this.name = Objects.requireNonNull(name, "Metric name cannot be null");
            return this;
        }

        public Builder withValue(double value) {
            this.value = Objects.requireNonNull(value, "Value cannot be null");
            return this;
        }

        public Builder withTimestamp(long timestamp) {
            this.timestamp = Objects.requireNonNull(timestamp, "Timestamp cannot be null");
            return this;
        }

        public ExportedMetric build() {
            return new ExportedMetric(this);
        }

    }
}
