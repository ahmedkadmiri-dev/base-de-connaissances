package com.example.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@Configuration
@EnableConfigurationProperties(ExceptionProperties.class)
public class ExceptionConfiguration {
}