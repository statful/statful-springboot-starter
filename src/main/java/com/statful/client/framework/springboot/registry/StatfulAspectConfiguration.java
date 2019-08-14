package com.statful.client.framework.springboot.registry;

import com.statful.client.aspects.StatfulAspect;
import com.statful.client.domain.api.StatfulClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

import static org.aspectj.lang.Aspects.aspectOf;

@Configuration
public class StatfulAspectConfiguration {

    @Resource
    private StatfulClient statfulClient;

    @Bean
    public StatfulAspect statfulAspect() {
        StatfulAspect statfulAspect = aspectOf(StatfulAspect.class);
        statfulAspect.setStatfulClient(statfulClient);
        return statfulAspect;
    }
}
