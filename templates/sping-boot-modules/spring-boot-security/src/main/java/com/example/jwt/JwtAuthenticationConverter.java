package com.example.jwt;

import com.example.properties.JwtProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import java.util.*;

@RequiredArgsConstructor
public class JwtAuthenticationConverter
        implements Converter<Jwt, AbstractAuthenticationToken> {

    private final JwtProperties properties;

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {

        Collection<GrantedAuthority> authorities = extractRoles(jwt);

        return new JwtAuthenticationToken(
                jwt,
                authorities,
                jwt.getSubject()
        );
    }

    private Collection<GrantedAuthority> extractRoles(Jwt jwt) {

        Set<GrantedAuthority> authorities = new LinkedHashSet<>();

        for (String path : properties.getRolesClaims()) {

            Object claim = resolveClaim(jwt.getClaims(), path);

            if (claim instanceof Collection<?> roles) {
                roles.stream()
                        .filter(String.class::isInstance)
                        .map(String.class::cast)
                        .map(role -> properties.getAuthorityPrefix() + role)
                        .map(SimpleGrantedAuthority::new)
                        .forEach(authorities::add);
            }
        }

        return authorities;
    }

    private Object resolveClaim(Map<String, Object> claims, String path) {

        Object current = claims;

        for (String key : path.split("\\.")) {

            if (!(current instanceof Map<?, ?> map)) {
                return null;
            }

            current = map.get(key);
        }

        return current;
    }
}