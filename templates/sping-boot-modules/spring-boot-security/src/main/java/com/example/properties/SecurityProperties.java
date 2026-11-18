package com.example.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ConfigurationProperties(prefix = "security")
public class SecurityProperties {

    private boolean enabled = true;

    private List<String> publicEndpoints = new ArrayList<>();

    private String provider = "KEYCLOAK";

    private Cors cors = new Cors();

    @Getter
    @Setter
    public static class Cors {

        private boolean enabled = false;

        private List<String> allowedOrigins = new ArrayList<>();

        private List<String> allowedMethods = new ArrayList<>();

        private List<String> allowedHeaders = new ArrayList<>();

        private boolean allowCredentials = false;
    }
}
