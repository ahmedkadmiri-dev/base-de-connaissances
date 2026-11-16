
package com.example.aspect;

import com.example.config.LoggingProperties;
import com.example.config.TraceContextProvider;
import com.example.utils.LoggingSanitizer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class RepositoryLoggingAspect {

    private final TraceContextProvider traceContextProvider;
    private final LoggingProperties loggingProperties;
    private final LoggingSanitizer sanitizer;

    @Around("@within(org.springframework.stereotype.Repository)")
    public Object logRepositoryExecution(ProceedingJoinPoint joinPoint) throws Throwable {

        LoggingProperties.Repository properties = loggingProperties.getRepository();

        if (!properties.isEnabled()) {
            return joinPoint.proceed();
        }

        long startTime = System.currentTimeMillis();

        String className = joinPoint.getSignature()
                .getDeclaringType()
                .getSimpleName();

        String methodName = joinPoint.getSignature().getName();

        String traceId = traceContextProvider.getTraceId();
        String idempotencyKey = traceContextProvider.getIdempotencyKey();

        // Repository START
        if (properties.isIncludeParameters()) {
            log.debug(
                    "REPOSITORY START | traceId={} | idempotencyKey={} | repository={} | method={} | input={}",
                    traceId,
                    idempotencyKey,
                    className,
                    methodName,
                    sanitizer.sanitize(joinPoint.getArgs(), properties.getMaxValueLength())
            );
        } else {
            log.debug(
                    "REPOSITORY START | traceId={} | idempotencyKey={} | repository={} | method={}",
                    traceId,
                    idempotencyKey,
                    className,
                    methodName
            );
        }

        try {
            Object result = joinPoint.proceed();

            long duration = System.currentTimeMillis() - startTime;

            // Repository SUCCESS
            log.debug(
                    "REPOSITORY SUCCESS | traceId={} | idempotencyKey={} | repository={} | method={} | duration={}ms",
                    traceId,
                    idempotencyKey,
                    className,
                    methodName,
                    duration
            );

            return result;

        } catch (Throwable exception) {

            long duration = System.currentTimeMillis() - startTime;

            log.error(
                    "REPOSITORY ERROR | traceId={} | idempotencyKey={} | repository={} | method={} | duration={}ms | exception={} | message={}",
                    traceId,
                    idempotencyKey,
                    className,
                    methodName,
                    duration,
                    exception.getClass().getSimpleName(),
                    exception.getMessage()
            );

            throw exception;
        }
    }
}