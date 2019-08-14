package com.statful.client.framework.springboot.registry;

import com.statful.client.core.http.StatfulFactory;
import com.statful.client.domain.api.StatfulClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StatfulClientAutoConfiguration {

    @Value("${statful.client.enabled:false}")
    public boolean enabled;

    @Value("${statful.client.workerPoolSize:1}")
    private int workerPoolSize;

    @Value("${statful.client.token:}")
    private String token;

    @Value("${statful.client.flush.size:10}")
    private int flushSize;

    @Value("${statful.client.flush.intervalMillis:5000}")
    private int flushIntervalSeconds;

    @Value("${statful.client.app:}")
    private String app;

    @Value("${statful.client.host:}")
    private String host;

    @Value("${statful.client.port:443}")
    private int port;

    @Value("${statful.client.secure:true}")
    public boolean secure;

    @Value("${statful.client.enableDryRun:false}")
    private boolean dryRun;

    @Value("${statful.client.sampleRate:100}")
    private int sampleRate;

    @Bean
    public StatfulClient statfulClient() {
        StatfulClient client = StatfulFactory.buildHTTPClient().with()
                .workerPoolSize(workerPoolSize)
                .host(host).port(port)
                .secure(secure)
                .token(token)
                .flushInterval(flushIntervalSeconds)
                .flushSize(flushSize)
                .sampleRate(sampleRate)
                .app(app)
                .isDryRun(dryRun)
                .build();

        if (!enabled) {
            client.disable();
        }
        return client;
    }
}
