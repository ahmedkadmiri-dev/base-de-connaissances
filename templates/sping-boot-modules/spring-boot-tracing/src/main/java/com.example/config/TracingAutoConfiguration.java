package com.example.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import io.micrometer.tracing.Tracer;
import org.springframework.context.annotation.PropertySource;

@AutoConfiguration
@ConditionalOnClass(Tracer.class)
@PropertySource("classpath:spring-boot-tracing-defaults.properties")
public class TracingAutoConfiguration {

    @Bean
    @ConditionalOnBean(Tracer.class)
    public TracingProvider tracingProvider(Tracer tracer) {
        return new TracingProvider(tracer);
    }
}