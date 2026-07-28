package com.example.Investigation_Tracking_Solution.dto.auditlog;

import com.example.Investigation_Tracking_Solution.model.AuditAction;
import com.example.Investigation_Tracking_Solution.model.AuditModule;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class AuditLogResponse {
    private Long id;
    private Long auditLogId;
    private AuditAction action;
    private AuditModule module;
    private String description;
    private Long entityId;
    private String entityType;
    private String ipAddress;
    private Long performedById;
    private String performedByName;
    private LocalDateTime createdAt;
}
