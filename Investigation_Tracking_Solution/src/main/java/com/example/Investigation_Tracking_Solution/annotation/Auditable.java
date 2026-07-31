package com.example.Investigation_Tracking_Solution.annotation;

import com.example.Investigation_Tracking_Solution.model.AuditAction;
import com.example.Investigation_Tracking_Solution.model.AuditModule;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Auditable {
    AuditAction action();
    AuditModule module();
    String entityType() default "";
    String description() default "";
}
