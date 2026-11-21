
package com.example.aspects;

import com.example.annotations.Auditable;
import com.example.enums.AuditStatus;
import com.example.models.AuditCommand;
import com.example.services.AuditService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.support.AopUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import tools.jackson.databind.ObjectMapper;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Collection;
import java.util.Map;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AuditAspect {

    private final AuditService auditService;
    private final ObjectMapper objectMapper;

    @Around("@annotation(auditable)")
    public Object audit(
            ProceedingJoinPoint joinPoint,
            Auditable auditable
    ) throws Throwable {

        String aggregateId = resolveAggregateId(joinPoint, auditable);
        Object requestBody = resolveRequestBody(joinPoint);

        Object result;

        try {
            result = joinPoint.proceed();

        } catch (Throwable exception) {

            try {
                auditService.record(new AuditCommand(
                        auditable.aggregateType(),
                        aggregateId,
                        auditable.action(),
                        requestBody,
                        null,
                        AuditStatus.FAILURE
                ));
            } catch (Exception auditException) {
                exception.addSuppressed(auditException);
                log.error("Failed to record audit event after business failure", auditException);
            }

            throw exception;
        }

        Object responseBody = resolveResponseBody(result);

        try {
            auditService.record(new AuditCommand(
                    auditable.aggregateType(),
                    aggregateId,
                    auditable.action(),
                    requestBody,
                    responseBody,
                    AuditStatus.SUCCESS
            ));
        } catch (Exception auditException) {
            log.error("Failed to record successful audit event", auditException);
        }

        return result;
    }

    private String resolveAggregateId(
            ProceedingJoinPoint joinPoint,
            Auditable auditable
    ) {
        if (!auditable.aggregateIdParam().isBlank()) {

            MethodSignature signature =
                    (MethodSignature) joinPoint.getSignature();

            String[] parameterNames = signature.getParameterNames();
            Object[] arguments = joinPoint.getArgs();

            for (int i = 0; i < parameterNames.length; i++) {
                if (parameterNames[i].equals(auditable.aggregateIdParam())) {
                    return arguments[i] != null
                            ? arguments[i].toString()
                            : null;
                }
            }

            throw new IllegalArgumentException(
                    "Audit parameter not found: " + auditable.aggregateIdParam()
            );
        }

        return auditable.aggregateId();
    }

    private Object resolveRequestBody(ProceedingJoinPoint joinPoint) {

        MethodSignature signature =
                (MethodSignature) joinPoint.getSignature();

        Method method = AopUtils.getMostSpecificMethod(
                signature.getMethod(),
                joinPoint.getTarget().getClass()
        );

        Parameter[] parameters = method.getParameters();
        Object[] arguments = joinPoint.getArgs();

        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i].isAnnotationPresent(RequestBody.class)) {
                return toAuditable(arguments[i]);
            }
        }

        return null;
    }

    private Object resolveResponseBody(Object result) {

        if (result == null) {
            return null;
        }

        if (result instanceof ResponseEntity<?> responseEntity) {
            return toAuditable(responseEntity.getBody());
        }

        return toAuditable(result);
    }

    /**
     * Convertit les objets en structures sérialisables.
     * Supporte les DTO, Map, Collection et tableaux.
     */
    private Object toAuditable(Object object) {

        if (object == null) {
            return null;
        }

        if (object instanceof Map<?, ?>
                || object instanceof Collection<?>
                || object.getClass().isArray()) {
            return object;
        }

        try {
            return objectMapper.convertValue(object, Object.class);
        } catch (Exception exception) {
            log.warn("Unable to convert audit data of type {}",
                    object.getClass().getName(), exception);
            return object.toString();
        }
    }
}
