package com.example.Investigation_Tracking_Solution.aspect;

import com.example.Investigation_Tracking_Solution.annotation.Auditable;
import com.example.Investigation_Tracking_Solution.model.AuditAction;
import com.example.Investigation_Tracking_Solution.model.AuditModule;
import com.example.Investigation_Tracking_Solution.model.User;
import com.example.Investigation_Tracking_Solution.service.AuditLogService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AuditLoggingAspect {

    private final AuditLogService auditLogService;

    @Around("@annotation(auditable)")
    public Object logAudit(ProceedingJoinPoint joinPoint, Auditable auditable) throws Throwable {
        Object result = joinPoint.proceed();

        try {
            recordAudit(joinPoint, auditable, result);
        } catch (Exception ex) {
            log.error("Automatic audit logging failed for action {}: {}", auditable.action(), ex.getMessage());
        }

        return result;
    }

    private void recordAudit(ProceedingJoinPoint joinPoint, Auditable auditable, Object result) {
        AuditModule module = auditable.module();
        AuditAction action = auditable.action();
        String entityType = !auditable.entityType().isEmpty() ? auditable.entityType() : module.name();

        Long entityId = extractEntityId(joinPoint, result);
        String description = buildDescription(auditable, entityId, result);
        User performedBy = getAuthenticatedUser();
        String ipAddress = getClientIpAddress();

        auditLogService.createAuditLog(
                module,
                action,
                description,
                performedBy,
                entityId,
                entityType,
                ipAddress
        );
    }

    private Long extractEntityId(ProceedingJoinPoint joinPoint, Object result) {
        if (result != null) {
            Long idFromReturn = extractIdFromObject(result);
            if (idFromReturn != null) {
                return idFromReturn;
            }
        }

        Object[] args = joinPoint.getArgs();
        if (args != null) {
            for (Object arg : args) {
                if (arg instanceof Long longArg) {
                    return longArg;
                }
            }
        }

        return null;
    }

    private Long extractIdFromObject(Object obj) {
        try {
            Method getIdMethod = obj.getClass().getMethod("getId");
            Object idVal = getIdMethod.invoke(obj);
            if (idVal instanceof Long l) {
                return l;
            }
        } catch (Exception ignored) {
        }

        try {
            Method getAttachmentIdMethod = obj.getClass().getMethod("getAttachmentId");
            Object idVal = getAttachmentIdMethod.invoke(obj);
            if (idVal instanceof Long l) {
                return l;
            }
        } catch (Exception ignored) {
        }

        return null;
    }

    private String buildDescription(Auditable auditable, Long entityId, Object result) {
        String baseDesc = auditable.description();
        if (baseDesc != null && !baseDesc.trim().isEmpty()) {
            if (entityId != null && !baseDesc.contains(String.valueOf(entityId))) {
                return baseDesc + " (ID: " + entityId + ")";
            }
            return baseDesc;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(auditable.action()).append(" operation on ").append(auditable.module());
        if (entityId != null) {
            sb.append(" with ID ").append(entityId);
        }
        return sb.toString();
    }

    private User getAuthenticatedUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getPrincipal() instanceof User user) {
                return user;
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    private String getClientIpAddress() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                String xForwardedFor = request.getHeader("X-Forwarded-For");
                if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
                    return xForwardedFor.split(",")[0].trim();
                }
                return request.getRemoteAddr();
            }
        } catch (Exception ignored) {
        }
        return "INTERNAL";
    }
}
