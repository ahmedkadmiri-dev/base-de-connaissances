package com.example.aspects;

import com.example.annotations.Auditable;
import com.example.enums.AuditStatus;
import com.example.models.AuditCommand;
import com.example.enums.AuditAction;
import com.example.services.AuditService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class AuditAspect {

    private final AuditService auditService;

    @Around("@annotation(auditable)")
    public Object audit(
            ProceedingJoinPoint joinPoint,
            Auditable auditable
    ) throws Throwable {

        try {
            Object result = joinPoint.proceed();

            auditService.record(new AuditCommand(
                    auditable.aggregateType(),
                    auditable.aggregateId(),
                    AuditAction.valueOf(auditable.action()),
                    null,
                    null,
                    AuditStatus.SUCCESS
            ));

            return result;

        } catch (Throwable exception) {

            auditService.record(new AuditCommand(
                    auditable.aggregateType(),
                    auditable.aggregateId(),
                    AuditAction.valueOf(auditable.action()),
                    null,
                    null,
                    AuditStatus.FAILURE
            ));

            throw exception;
        }
    }
}