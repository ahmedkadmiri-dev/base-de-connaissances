package com.example.providers;

import com.example.context.CurrentPrincipal;
import com.example.models.AuditContext;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;

@RequiredArgsConstructor
public class AuditContextProvider {

    private static final String NO_AUTH = "NO_AUTH";
    private static final String IDEMPOTENCY_HEADER = "Idempotency-Key";
    private static final String FORWARDED_FOR_HEADER = "X-Forwarded-For";

    private final String applicationName;
    private final Tracer tracer;
    private final ObjectProvider<HttpServletRequest> requestProvider;

    public AuditContext getCurrentContext() {
        HttpServletRequest request = requestProvider.getIfAvailable();

        return new AuditContext(
                CurrentPrincipal.getSubject().orElse(NO_AUTH),
                CurrentPrincipal.getUsername().orElse(null),
                applicationName,
                getCurrentTraceId(),
                getIdempotencyKey(request),
                getClientIpAddress(request),
                getCallerIpAddress(request)
        );
    }

    private String getCurrentTraceId() {
        Span currentSpan = tracer.currentSpan();

        return currentSpan != null
                ? currentSpan.context().traceId()
                : null;
    }

    private String getIdempotencyKey(HttpServletRequest request) {
        return request != null
                ? request.getHeader(IDEMPOTENCY_HEADER)
                : null;
    }

    /**
     * IP du client final ayant initié la requête.
     * À condition que le proxy de confiance renseigne X-Forwarded-For.
     */
    private String getClientIpAddress(HttpServletRequest request) {
        if (request == null) {
            return null;
        }

        String forwardedFor = request.getHeader(FORWARDED_FOR_HEADER);

        if (forwardedFor != null && !forwardedFor.isBlank()) {
            return forwardedFor.split(",")[0].trim();
        }

        return request.getRemoteAddr();
    }

    /**
     * IP du composant qui appelle directement le backend.
     */
    private String getCallerIpAddress(HttpServletRequest request) {
        return request != null
                ? request.getRemoteAddr()
                : null;
    }
}