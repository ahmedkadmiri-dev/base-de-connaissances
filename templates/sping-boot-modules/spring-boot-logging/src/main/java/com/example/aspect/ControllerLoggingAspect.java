
package com.example.aspect;

import com.example.config.LoggingProperties;
import com.example.config.TraceContextProvider;
import com.example.utils.LoggingSanitizer;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class ControllerLoggingAspect {

    private static final Set<String> SENSITIVE_HEADERS = Set.of(
            "authorization",
            "proxy-authorization",
            "cookie",
            "set-cookie",
            "x-api-key"
    );

    private final TraceContextProvider traceContextProvider;
    private final LoggingSanitizer sanitizer;
    private final LoggingProperties loggingProperties;

    @Around("@within(org.springframework.web.bind.annotation.RestController)")
    public Object logControllerExecution(ProceedingJoinPoint joinPoint) throws Throwable {

        LoggingProperties.Controller properties = loggingProperties.getController();

        if (!properties.isEnabled()) {
            return joinPoint.proceed();
        }

        long startTime = System.currentTimeMillis();

        String traceId = traceContextProvider.getTraceId();
        String idempotencyKey = traceContextProvider.getIdempotencyKey();

        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();

        ServletRequestAttributes attributes =
                RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes servletAttributes
                        ? servletAttributes
                        : null;

        HttpServletRequest request = attributes != null ? attributes.getRequest() : null;
        HttpServletResponse response = attributes != null ? attributes.getResponse() : null;

        Map<String, String> headers = properties.isIncludeHeaders()
                ? extractHeaders(request)
                : Collections.emptyMap();

        Map<String, Object> requestDetails = properties.isIncludeRequestBody()
                ? extractRequestDetails(joinPoint)
                : Collections.emptyMap();

        log.info(
                "HTTP REQUEST START | traceId={} | idempotencyKey={} | controller={} | method={} | httpMethod={} | uri={} | query={} | headers={} | requestDetails={}",
                traceId,
                idempotencyKey,
                className,
                methodName,
                request != null ? request.getMethod() : "N/A",
                request != null ? request.getRequestURI() : "N/A",
                request != null ? request.getQueryString() : "N/A",
                sanitizer.sanitize(headers, properties.getMaxBodyLength()),
                sanitizer.sanitize(requestDetails, properties.getMaxBodyLength())
        );

        try {
            Object result = joinPoint.proceed();

            long duration = System.currentTimeMillis() - startTime;

            Object responseDetails = properties.isIncludeResponseBody()
                    ? sanitizer.sanitize(result, properties.getMaxBodyLength())
                    : "DISABLED";

            log.info(
                    "HTTP REQUEST SUCCESS | traceId={} | idempotencyKey={} | controller={} | method={} | status={} | duration={}ms | response={}",
                    traceId,
                    idempotencyKey,
                    className,
                    methodName,
                    response != null ? response.getStatus() : "N/A",
                    duration,
                    responseDetails
            );

            return result;

        } catch (Throwable exception) {

            long duration = System.currentTimeMillis() - startTime;

            log.error(
                    "HTTP REQUEST ERROR | traceId={} | idempotencyKey={} | controller={} | method={} | status={} | duration={}ms | exception={} | message={}",
                    traceId,
                    idempotencyKey,
                    className,
                    methodName,
                    response != null ? response.getStatus() : "N/A",
                    duration,
                    exception.getClass().getSimpleName(),
                    sanitizer.sanitize(exception.getMessage(), properties.getMaxBodyLength())
            );

            throw exception;
        }
    }

    /**
     * Extracts exclusively the argument annotated with @RequestBody.
     */
    private Map<String, Object> extractRequestDetails(ProceedingJoinPoint joinPoint) {

        Map<String, Object> details = new LinkedHashMap<>();

        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        Parameter[] parameters = method.getParameters();
        Object[] arguments = joinPoint.getArgs();

        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i].isAnnotationPresent(RequestBody.class)) {
                details.put("body", arguments[i]);
                break;
            }
        }

        return details;
    }

    /**
     * Extracts HTTP headers while excluding sensitive headers.
     */
    private Map<String, String> extractHeaders(HttpServletRequest request) {

        Map<String, String> headers = new LinkedHashMap<>();

        if (request == null || request.getHeaderNames() == null) {
            return headers;
        }

        Collections.list(request.getHeaderNames()).forEach(headerName -> {
            if (!SENSITIVE_HEADERS.contains(headerName.toLowerCase(Locale.ROOT))) {
                headers.put(headerName, request.getHeader(headerName));
            }
        });

        return headers;
    }
}