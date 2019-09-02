package com.statful.client.framework.springboot;

import com.statful.client.domain.api.StatfulClient;
import com.statful.micrometer.registry.StatfulMetricRegistry;
import com.statful.micrometer.registry.StatfulMetricRegistryConfig;
import com.statful.micrometer.registry.StatfulMetricsProperties;
import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import javax.annotation.Resource;

@Configuration
@EnableConfigurationProperties(value = StatfulMetricsProperties.class)
@ComponentScan("com.statful.micrometer")
public class StatfulRegistryAutoConfiguration {

    @Resource
    private StatfulMetricRegistryConfig statfulMetricRegistryConfig;

    @Resource
    private StatfulClient statfulClient;

    @Bean
    @ConditionalOnBean(StatfulClient.class)
    public MeterRegistry metricRegistry(StatfulMetricsProperties properties) {
        return new StatfulMetricRegistry(statfulMetricRegistryConfig, statfulClient, Clock.SYSTEM, properties);
    }
}
