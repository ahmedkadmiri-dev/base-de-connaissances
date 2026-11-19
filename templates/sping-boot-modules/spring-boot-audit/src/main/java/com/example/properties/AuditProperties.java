package com.example.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "audit.datasource")
public class AuditProperties {

    private String url;

    private String driverClassName;

    private String username;

    private String password;
}