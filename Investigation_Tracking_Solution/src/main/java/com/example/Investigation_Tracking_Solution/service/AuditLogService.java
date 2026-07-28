package com.example.Investigation_Tracking_Solution.service;

import com.example.Investigation_Tracking_Solution.dto.auditlog.AuditLogResponse;
import com.example.Investigation_Tracking_Solution.model.AuditAction;
import com.example.Investigation_Tracking_Solution.model.AuditModule;
import com.example.Investigation_Tracking_Solution.model.User;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

public interface AuditLogService {
    AuditLogResponse createAuditLog(AuditModule module, AuditAction action, String description, User performedBy, Long entityId, String entityType, String ipAddress);
    AuditLogResponse createAuditLog(AuditModule module, AuditAction action, String description, Long entityId, String entityType, String ipAddress);
    AuditLogResponse getAuditLogById(Long id);
    Page<AuditLogResponse> getAllAuditLogs(int page, int size);
    Page<AuditLogResponse> getLogsByUser(Long userId, int page, int size);
    Page<AuditLogResponse> getLogsByModule(AuditModule module, int page, int size);
    Page<AuditLogResponse> getLogsByAction(AuditAction action, int page, int size);
    Page<AuditLogResponse> getLogsByDateRange(LocalDateTime startDate, LocalDateTime endDate, int page, int size);
    Page<AuditLogResponse> searchLogs(String keyword, int page, int size);
    void deleteAuditLog(Long id);
}
