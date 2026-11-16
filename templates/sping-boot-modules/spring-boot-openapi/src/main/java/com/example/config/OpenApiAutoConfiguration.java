package com.example.config;

import com.example.model.ErrorResponse;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.customizers.GlobalOpenApiCustomizer;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@EnableConfigurationProperties(OpenApiProperties.class)
@RequiredArgsConstructor
public class OpenApiAutoConfiguration {

    private final OpenApiProperties properties;

    @Bean
    public OperationCustomizer openApiOperationCustomizer() {
        return new OpenApiOperationCustomizer();
    }

    @Bean
    @ConditionalOnMissingBean(OpenAPI.class)
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title(properties.getTitle())
                        .description(properties.getDescription())
                        .version(properties.getVersion()));
    }

    @Bean
    public GlobalOpenApiCustomizer errorResponseSchemaCustomizer() {
        return openApi -> {
            if (openApi.getComponents() == null) {
                openApi.setComponents(new Components());
            }

            ModelConverters.getInstance()
                    .read(ErrorResponse.class)
                    .forEach((name, schema) -> {
                        if (openApi.getComponents().getSchemas() == null
                                || !openApi.getComponents().getSchemas().containsKey(name)) {
                            openApi.getComponents().addSchemas(name, schema);
                        }
                    });
        };
    }
}