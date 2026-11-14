package com.example.config;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
@RequiredArgsConstructor
public class TraceContextProvider {

    private static final String IDEMPOTENCY_HEADER = "Idempotency-Key";

    private final Tracer tracer;

    public String getTraceId() {

        Span currentSpan = tracer.currentSpan();

        return currentSpan != null
                ? currentSpan.context().traceId()
                : "N/A";
    }

    public String getIdempotencyKey() {

        if (RequestContextHolder.getRequestAttributes()
                instanceof ServletRequestAttributes attributes) {

            return attributes.getRequest()
                    .getHeader(IDEMPOTENCY_HEADER);
        }

        return null;
    }
}
