package com.example.config;

import com.example.handler.CustomAccessDeniedHandler;
import com.example.handler.CustomAuthenticationEntryPoint;
import com.example.jwt.JwtAuthenticationConverter;
import com.example.jwt.JwtProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@AutoConfiguration
@RequiredArgsConstructor
@EnableConfigurationProperties({
        SecurityProperties.class,
        JwtProperties.class
})
@ConditionalOnProperty(
        prefix = "security",
        name = "enabled",
        havingValue = "true",
        matchIfMissing = true
)
public class SecurityAutoConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            SecurityProperties properties,
            JwtAuthenticationConverter jwtAuthenticationConverter,
            CustomAuthenticationEntryPoint authenticationEntryPoint,
            CustomAccessDeniedHandler accessDeniedHandler
    ) throws Exception {

        http
                .cors(cors -> {})
                .csrf(csrf -> csrf.disable())
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())

                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                                properties.getPublicEndpoints().toArray(String[]::new)
                        ).permitAll()
                        .anyRequest().authenticated()
                )

                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler)
                )

                .oauth2ResourceServer(oauth2 -> oauth2
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler)
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(jwtAuthenticationConverter)
                        )
                );

        return http.build();
    }

    @Bean
    @ConditionalOnMissingBean
    public CustomAuthenticationEntryPoint authenticationEntryPoint(
            ObjectMapper objectMapper
    ) {
        return new CustomAuthenticationEntryPoint(objectMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public CustomAccessDeniedHandler accessDeniedHandler(
            ObjectMapper objectMapper
    ) {
        return new CustomAccessDeniedHandler(objectMapper);
    }
}
