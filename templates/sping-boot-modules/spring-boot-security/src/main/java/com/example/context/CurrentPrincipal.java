package com.example.context;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Map;
import java.util.Optional;

public final class CurrentPrincipal {

    private CurrentPrincipal() {
    }

    private static Optional<Authentication> getAuthentication() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .filter(Authentication::isAuthenticated);
    }

    public static Optional<String> getUsername() {
        return getClaim("preferred_username");
    }

    public static Optional<String> getUserId() {
        return getClaim("sub");
    }

    public static Optional<String> getClaim(String claim) {
        return getAuthentication()
                .filter(JwtAuthenticationToken.class::isInstance)
                .map(JwtAuthenticationToken.class::cast)
                .map(authentication -> authentication.getToken().getClaimAsString(claim));
    }

    public static Optional<Map<String, Object>> getClaims() {
        return getAuthentication()
                .filter(JwtAuthenticationToken.class::isInstance)
                .map(JwtAuthenticationToken.class::cast)
                .map(authentication -> authentication.getToken().getClaims());
    }
}
