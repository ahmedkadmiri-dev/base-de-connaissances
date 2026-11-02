package com.example.config;

import com.example.handler.GlobalExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

@Configuration
@EnableConfigurationProperties(ExceptionProperties.class)
@Import(GlobalExceptionHandler.class)
public class ExceptionConfiguration {
}