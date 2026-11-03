package com.example.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "exception")
public class ExceptionProperties {

    private boolean includeStacktrace = false;

    private boolean includeMessage = false;

    private boolean includeException = false;
}