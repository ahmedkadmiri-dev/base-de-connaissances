package com.example.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "security.jwt")
public class JwtProperties {

    /**
     * Claim contenant les rôles dans le JWT.
     */
    private String rolesClaim = "realm_access.roles";

    /**
     * Préfixe ajouté aux rôles.
     */
    private String authorityPrefix = "ROLE_";
}
