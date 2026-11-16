package com.example.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "spring.openapi")
public class OpenApiProperties {

    private boolean enabled = true;

    private boolean includeInternalServerError = true;

    private boolean includeSecurityResponses = true;

    private String title = "REST API";

    private String description = "REST API documentation";

    private String version = "1.0.0";
}