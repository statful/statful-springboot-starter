package com.statful.client.framework.springboot.config;

import com.statful.client.domain.api.StatfulClient;
import com.statful.client.framework.springboot.writer.StatfulMetricWriter;
import org.springframework.boot.actuate.autoconfigure.ExportMetricWriter;
import org.springframework.boot.actuate.endpoint.MetricsEndpoint;
import org.springframework.boot.actuate.endpoint.MetricsEndpointMetricReader;
import org.springframework.boot.actuate.metrics.writer.MetricWriter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * Main springboot client configuration class.
 *
 * Responsible for initializing metrics reader/writer.
 */
@Configuration
@ConfigurationProperties(prefix = "statful.client.springboot")
@ConditionalOnBean(value = {StatfulClient.class, MetricWriter.class})
public class SpringbootClientConfiguration {

    private Metrics metrics;

    public Metrics getMetrics() {
        return metrics;
    }

    public void setMetrics(Metrics metrics) {
        this.metrics = metrics;
    }

    /**
     * Meta-configuration class.
     */
    public static class Metrics {
        private List<Tags> tags = new ArrayList<>();

        public List<Tags> getTags() {
            return new ArrayList<>(tags);
        }

        public void setTags(List<Tags> tags) {
            this.tags = tags;
        }

        /**
         * Meta-configuration class.
         */
        public static class Tags {
            private String name;
            private String value;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }
        }
    }

    /**
     * Enable a metrics reader.
     * @param metricsEndpoint {@link MetricsEndpoint}
     * @return
     */
    @Bean
    @ConditionalOnProperty(value = "statful.client.springboot.metrics.enabled", havingValue = "true")
    public MetricsEndpointMetricReader metricsEndpointMetricReader(final MetricsEndpoint metricsEndpoint) {
        return new MetricsEndpointMetricReader(metricsEndpoint);
    }

    /**
     * Export a new {@link StatfulMetricWriter} metrics writer.
     * @return {@link MetricWriter}
     */
    @Bean
    @ExportMetricWriter
    MetricWriter metricWriter() {
        return new StatfulMetricWriter();
    }
}
