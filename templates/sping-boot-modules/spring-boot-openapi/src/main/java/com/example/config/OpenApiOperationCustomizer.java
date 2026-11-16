
package com.example.config;

import com.example.model.ErrorResponse;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.method.HandlerMethod;

import java.util.Arrays;

public class OpenApiOperationCustomizer implements OperationCustomizer {

    private static final String APPLICATION_JSON = "application/json";

    private static final String ERROR_SCHEMA_REF =
            "#/components/schemas/ErrorResponse";

    @Override
    public Operation customize(Operation operation, HandlerMethod handlerMethod) {

        ApiResponses responses = operation.getResponses();

        if (responses == null) {
            responses = new ApiResponses();
            operation.setResponses(responses);
        }

        // Supprimer le 200 généré automatiquement s'il n'est pas explicitement déclaré
        if (!hasExplicitOkResponse(handlerMethod)) {
            responses.remove("200");
        }

        // Réponse 500 pour toutes les opérations
        addResponseIfAbsent(responses, "500", "Internal Server Error");

        // Réponses de sécurité uniquement pour les endpoints @ApiSecured
        if (isSecured(handlerMethod)) {
            addResponseIfAbsent(responses, "401", "Unauthorized");
            addResponseIfAbsent(responses, "403", "Forbidden");
        }

        return operation;
    }

    private boolean isSecured(HandlerMethod handlerMethod) {
        return AnnotatedElementUtils.hasAnnotation(
                handlerMethod.getMethod(), ApiSecured.class)
                || AnnotatedElementUtils.hasAnnotation(
                handlerMethod.getBeanType(), ApiSecured.class);
    }

    private void addResponseIfAbsent(
            ApiResponses responses,
            String status,
            String description) {

        if (responses.containsKey(status)) {
            return;
        }

        ApiResponse response = new ApiResponse()
                .description(description)
                .content(createErrorContent());

        responses.addApiResponse(status, response);
    }

    private Content createErrorContent() {
        return new Content().addMediaType(
                APPLICATION_JSON,
                new MediaType().schema(
                        new Schema<ErrorResponse>().$ref(ERROR_SCHEMA_REF)
                )
        );
    }

    private boolean hasExplicitOkResponse(HandlerMethod handlerMethod) {

        io.swagger.v3.oas.annotations.Operation operation =
                AnnotatedElementUtils.findMergedAnnotation(
                        handlerMethod.getMethod(),
                        io.swagger.v3.oas.annotations.Operation.class);

        if (operation != null && Arrays.stream(operation.responses())
                .anyMatch(response -> "200".equals(response.responseCode()))) {
            return true;
        }

        return AnnotatedElementUtils.getMergedRepeatableAnnotations(
                        handlerMethod.getMethod(),
                        io.swagger.v3.oas.annotations.responses.ApiResponse.class)
                .stream()
                .anyMatch(response -> "200".equals(response.responseCode()));
    }
}