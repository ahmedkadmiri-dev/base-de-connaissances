package com.example.config;

import com.example.properties.SecurityProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Collection;

@AutoConfiguration
@EnableConfigurationProperties(SecurityProperties.class)
@ConditionalOnProperty(
        prefix = "security.cors",
        name = "enabled",
        havingValue = "true"
)
@RequiredArgsConstructor
public class CorsAutoConfiguration {

    private final SecurityProperties properties;

    @Bean
    public CorsFilter corsFilter() {
        SecurityProperties.Cors corsProperties = properties.getCors();

        CorsConfiguration configuration = new CorsConfiguration();

        if(isNotEmpty(corsProperties.getAllowedOrigins()))
            configuration.setAllowedOrigins(corsProperties.getAllowedOrigins());
        if(isNotEmpty(corsProperties.getAllowedMethods()))
            configuration.setAllowedMethods(corsProperties.getAllowedMethods());
        if(isNotEmpty(corsProperties.getAllowedHeaders()))
            configuration.setAllowedHeaders(corsProperties.getAllowedHeaders());

        configuration.setAllowCredentials(corsProperties.isAllowCredentials());

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return new CorsFilter(source);
    }

    private boolean isNotEmpty(Collection<?> collection) {
        return collection != null && !collection.isEmpty();
    }
}
