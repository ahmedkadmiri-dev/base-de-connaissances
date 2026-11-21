package com.example.annotations;

import com.example.enums.AuditAction;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Auditable {

    AuditAction action();

    String aggregateType();

    String aggregateId() default "";

    String aggregateIdParam() default "";
}