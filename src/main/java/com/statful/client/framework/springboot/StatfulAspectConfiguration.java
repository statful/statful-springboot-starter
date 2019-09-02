package com.statful.client.framework.springboot;

import com.statful.client.aspects.StatfulAspect;
import com.statful.client.domain.api.StatfulClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

import static org.aspectj.lang.Aspects.aspectOf;

@Configuration
public class StatfulAspectConfiguration {

    @Resource
    private StatfulClient statfulClient;

    @Bean
    @ConditionalOnBean(StatfulClient.class)
    public StatfulAspect statfulAspect() {
        StatfulAspect statfulAspect = aspectOf(StatfulAspect.class);
        statfulAspect.setStatfulClient(statfulClient);
        return statfulAspect;
    }
}
