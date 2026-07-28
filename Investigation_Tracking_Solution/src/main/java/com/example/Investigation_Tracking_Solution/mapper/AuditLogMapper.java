package com.example.Investigation_Tracking_Solution.mapper;

import com.example.Investigation_Tracking_Solution.dto.auditlog.AuditLogResponse;
import com.example.Investigation_Tracking_Solution.model.AuditLog;

public class AuditLogMapper {

    public static AuditLogResponse toResponse(AuditLog auditLog) {
        String userName = null;
        if (auditLog.getPerformedBy() != null) {
            userName = auditLog.getPerformedBy().getFirstName() + " "
                    + auditLog.getPerformedBy().getLastName();
        }

        return AuditLogResponse.builder()
                .id(auditLog.getId())
                .auditLogId(auditLog.getId())
                .action(auditLog.getAction())
                .module(auditLog.getModule())
                .description(auditLog.getDescription())
                .entityId(auditLog.getEntityId())
                .entityType(auditLog.getEntityType())
                .ipAddress(auditLog.getIpAddress())
                .performedById(auditLog.getPerformedBy() != null ? auditLog.getPerformedBy().getId() : null)
                .performedByName(userName)
                .createdAt(auditLog.getCreatedAt())
                .build();
    }
}
