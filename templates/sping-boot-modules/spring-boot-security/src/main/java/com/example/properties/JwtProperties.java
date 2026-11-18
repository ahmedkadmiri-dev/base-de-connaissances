package com.example.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ConfigurationProperties(prefix = "security.jwt")
public class JwtProperties {

    private List<String> rolesClaims = new ArrayList<>(List.of(
            "realm_access.roles"
    ));

    private String authorityPrefix = "ROLE_";
}
