package com.statful.client.framework.springboot.common;

import java.util.Date;
import java.util.Objects;

public class ExportedMetric {
    private String name;
    private Number value;
    private Date timestamp;

    public ExportedMetric(Builder builder) {
        this.name = builder.name;
        this.value = builder.value;
        this.timestamp = builder.timestamp;
    }

    public String getName() {
        return name;
    }

    public Number getValue() {
        return value;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public static final class Builder {

        private String name;
        private Number value;
        private Date timestamp;

        public Builder withName(String name) {
            this.name = Objects.requireNonNull(name, "Metric name cannot be null");
            return this;
        }

        public Builder withValue(Number value) {
            this.value = Objects.requireNonNull(value, "Value cannot be null");
            return this;
        }

        public Builder withTimestamp(Date timestamp) {
            this.timestamp = Objects.requireNonNull(timestamp, "Timestamp cannot be null");
            return this;
        }

        public ExportedMetric build() {
            return new ExportedMetric(this);
        }

    }
}
