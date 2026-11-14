package com.example.config;

import com.example.handler.GlobalExceptionHandler;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@EnableConfigurationProperties(ExceptionProperties.class)
@Import(GlobalExceptionHandler.class)
public class ExceptionConfiguration {
}