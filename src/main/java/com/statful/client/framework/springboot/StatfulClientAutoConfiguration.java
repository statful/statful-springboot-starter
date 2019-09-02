package com.statful.client.framework.springboot;

import com.statful.client.core.api.ConfigurationBuilder;
import com.statful.client.core.api.StatfulClientBuilder;
import com.statful.client.domain.api.StatfulClient;
import com.statful.client.domain.api.Transport;
import com.statful.client.framework.springboot.properties.StatfulClientProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(value = StatfulClientProperties.class)
public class StatfulClientAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(StatfulClient.class)
    @ConditionalOnProperty(value = "statful.client.token")
    public StatfulClient statfulClient(StatfulClientProperties statfulClientProperties) {

        StatfulClientBuilder clientBuilder;

        if (statfulClientProperties.getTransport().equals(Transport.HTTP)) {
            clientBuilder = com.statful.client.core.http.StatfulFactory.buildHTTPClient();
        } else {
            clientBuilder = com.statful.client.core.udp.StatfulFactory.buildUDPClient();
        }

        ConfigurationBuilder<StatfulClient> configurationBuilder = clientBuilder.with()
                .workerPoolSize(statfulClientProperties.getWorkerPoolSize())
                .host(statfulClientProperties.getHost())
                .port(statfulClientProperties.getPort())
                .token(statfulClientProperties.getToken())
                .flushInterval(statfulClientProperties.getFlushInterval())
                .flushSize(statfulClientProperties.getFlushSize())
                .sampleRate(statfulClientProperties.getSampleRate())
                .namespace(statfulClientProperties.getNamespace())
                .timeoutMs(statfulClientProperties.getTimeout())
                .secure(statfulClientProperties.isSecure())
                .connectionPoolSize(statfulClientProperties.getConnectPoolSize())
                .connectionTimeoutMs(statfulClientProperties.getConnectTimeout())
                .app(statfulClientProperties.getApp())
                .isDryRun(statfulClientProperties.isDryRun());

        statfulClientProperties.getTags().forEach(configurationBuilder::tag);

        StatfulClient client = configurationBuilder.build();

        if (!statfulClientProperties.isEnabled()) {
            client.disable();
        }

        return client;
    }
}
