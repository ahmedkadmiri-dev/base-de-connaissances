package com.example.providers;

import com.example.models.AuditContext;
import com.example.context.CurrentPrincipal;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;

@RequiredArgsConstructor
public class AuditContextProvider {

    private static final String NO_AUTH = "NO_AUTH";
    private static final String IDEMPOTENCY_HEADER = "Idempotency-Key";

    private final String applicationName;
    private final Tracer tracer;
    private final ObjectProvider<HttpServletRequest> requestProvider;

    public AuditContext getCurrentContext() {
        return new AuditContext(
                CurrentPrincipal.getSubject().orElse(NO_AUTH),
                CurrentPrincipal.getUsername().orElse(null),
                applicationName,
                getCurrentTraceId(),
                getIdempotencyKey()
        );
    }

    private String getCurrentTraceId() {
        Span currentSpan = tracer.currentSpan();

        return currentSpan != null
                ? currentSpan.context().traceId()
                : null;
    }

    private String getIdempotencyKey() {
        HttpServletRequest request = requestProvider.getIfAvailable();

        return request != null
                ? request.getHeader(IDEMPOTENCY_HEADER)
                : null;
    }
}