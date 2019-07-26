package com.statful.client.framework.springboot.registry;

import com.statful.client.domain.api.StatfulClient;
import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import statful.registry.StatfulMetricRegistry;
import statful.registry.StatfulMetricRegistryConfig;
import statful.registry.StatfulMetricsProperties;

import javax.annotation.Resource;

@Configuration
@EnableConfigurationProperties(value = StatfulMetricsProperties.class)
public class StatfulRegistryAutoConfiguration {

    @Resource
    private StatfulMetricRegistryConfig statfulMetricRegistryConfig;

    @Resource
    private StatfulClient statfulClient;

    @Bean
    public MeterRegistry metricRegistry(StatfulMetricsProperties properties) {
        return new StatfulMetricRegistry(statfulMetricRegistryConfig, statfulClient, Clock.SYSTEM, properties);
    }
}
