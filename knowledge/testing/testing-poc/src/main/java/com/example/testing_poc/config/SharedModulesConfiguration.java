package com.example.testing_poc.config;

import com.example.config.ExceptionConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        ExceptionConfiguration.class,
        // SecurityConfiguration.class,
        // LoggingConfiguration.class,
        // AuditConfiguration.class
})
public class SharedModulesConfiguration {
}