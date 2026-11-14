
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

        // Vérifier si la journalisation du repository est activée
        if (!properties.isEnabled()) {
            return joinPoint.proceed();
        }

        long startTime = System.currentTimeMillis();

        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();

        String traceId = traceContextProvider.getTraceId();
        String idempotencyKey = traceContextProvider.getIdempotencyKey();

        // Journalisation des arguments
        if (properties.isIncludeParameters()) {
            log.debug(
                    "REPOSITORY START | traceId={} | idempotencyKey={} | repository={} | method={} | arguments={}",
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

            // Journalisation du résultat
            if (properties.isIncludeResult()) {
                log.debug(
                        "REPOSITORY SUCCESS | traceId={} | idempotencyKey={} | repository={} | method={} | duration={}ms | result={}",
                        traceId,
                        idempotencyKey,
                        className,
                        methodName,
                        duration,
                        sanitizer.sanitize(result, properties.getMaxValueLength())
                );
            } else {
                log.debug(
                        "REPOSITORY SUCCESS | traceId={} | idempotencyKey={} | repository={} | method={} | duration={}ms",
                        traceId,
                        idempotencyKey,
                        className,
                        methodName,
                        duration
                );
            }

            return result;

        } catch (Throwable exception) {

            long duration = System.currentTimeMillis() - startTime;

            log.error(
                    "REPOSITORY ERROR | traceId={} | idempotencyKey={} | repository={} | method={} | duration={}ms | exception={}",
                    traceId,
                    idempotencyKey,
                    className,
                    methodName,
                    duration,
                    exception
            );

            throw exception;
        }
    }
}