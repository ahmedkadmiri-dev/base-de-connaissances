
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
public class ServiceLoggingAspect {

    private final TraceContextProvider traceContextProvider;
    private final LoggingProperties loggingProperties;
    private final LoggingSanitizer sanitizer;

    @Around("@within(org.springframework.stereotype.Service)")
    public Object logServiceExecution(ProceedingJoinPoint joinPoint) throws Throwable {

        LoggingProperties.Service properties = loggingProperties.getService();

        // Vérifier si la journalisation du service est activée
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
            log.info(
                    "SERVICE START | traceId={} | idempotencyKey={} | service={} | method={} | arguments={}",
                    traceId,
                    idempotencyKey,
                    className,
                    methodName,
                    sanitizer.sanitize(joinPoint.getArgs(), properties.getMaxValueLength())
            );
        } else {
            log.info(
                    "SERVICE START | traceId={} | idempotencyKey={} | service={} | method={}",
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
                log.info(
                        "SERVICE SUCCESS | traceId={} | idempotencyKey={} | service={} | method={} | duration={}ms | result={}",
                        traceId,
                        idempotencyKey,
                        className,
                        methodName,
                        duration,
                        sanitizer.sanitize(result, properties.getMaxValueLength())
                );
            } else {
                log.info(
                        "SERVICE SUCCESS | traceId={} | idempotencyKey={} | service={} | method={} | duration={}ms",
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
                    "SERVICE ERROR | traceId={} | idempotencyKey={} | service={} | method={} | duration={}ms | exception={}",
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