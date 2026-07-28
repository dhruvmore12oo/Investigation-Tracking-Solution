package com.example.Investigation_Tracking_Solution.service.impl;

import com.example.Investigation_Tracking_Solution.dto.auditlog.AuditLogResponse;
import com.example.Investigation_Tracking_Solution.exception.ResourceNotFoundException;
import com.example.Investigation_Tracking_Solution.mapper.AuditLogMapper;
import com.example.Investigation_Tracking_Solution.model.*;
import com.example.Investigation_Tracking_Solution.repository.AuditLogRepo;
import com.example.Investigation_Tracking_Solution.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepo auditLogRepository;

    @Override
    public AuditLogResponse createAuditLog(AuditModule module, AuditAction action, String description, User performedBy, Long entityId, String entityType, String ipAddress) {
        AuditLog auditLog = AuditLog.builder()
                .module(module)
                .action(action)
                .description(description)
                .performedBy(performedBy)
                .entityId(entityId)
                .entityType(entityType)
                .ipAddress(ipAddress)
                .build();

        AuditLog saved = auditLogRepository.save(auditLog);
        return AuditLogMapper.toResponse(saved);
    }

    @Override
    public AuditLogResponse createAuditLog(AuditModule module, AuditAction action, String description, Long entityId, String entityType, String ipAddress) {
        User currentUser = getCurrentUserQuietly();
        return createAuditLog(module, action, description, currentUser, entityId, entityType, ipAddress);
    }

    @Override
    public AuditLogResponse getAuditLogById(Long id) {
        AuditLog log = findAuditLogById(id);
        return AuditLogMapper.toResponse(log);
    }

    @Override
    public Page<AuditLogResponse> getAllAuditLogs(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return auditLogRepository.findAll(pageable).map(AuditLogMapper::toResponse);
    }

    @Override
    public Page<AuditLogResponse> getLogsByUser(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return auditLogRepository.findByPerformedBy_Id(userId, pageable).map(AuditLogMapper::toResponse);
    }

    @Override
    public Page<AuditLogResponse> getLogsByModule(AuditModule module, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return auditLogRepository.findByModule(module, pageable).map(AuditLogMapper::toResponse);
    }

    @Override
    public Page<AuditLogResponse> getLogsByAction(AuditAction action, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return auditLogRepository.findByAction(action, pageable).map(AuditLogMapper::toResponse);
    }

    @Override
    public Page<AuditLogResponse> getLogsByDateRange(LocalDateTime startDate, LocalDateTime endDate, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return auditLogRepository.findByCreatedAtBetween(startDate, endDate, pageable).map(AuditLogMapper::toResponse);
    }

    @Override
    public Page<AuditLogResponse> searchLogs(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return auditLogRepository.searchByKeyword(keyword, pageable).map(AuditLogMapper::toResponse);
    }

    @Override
    public void deleteAuditLog(Long id) {
        AuditLog log = findAuditLogById(id);
        User currentUser = getCurrentUser();

        if (currentUser.getRole() != Role.ADMIN) {
            throw new AccessDeniedException("Only ADMIN can delete audit logs.");
        }

        auditLogRepository.delete(log);
    }

    private AuditLog findAuditLogById(Long id) {
        return auditLogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Could Not Find Audit Log With Id : " + id));
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof User)) {
            throw new AccessDeniedException("Authenticated user is required for this operation.");
        }
        return (User) authentication.getPrincipal();
    }

    private User getCurrentUserQuietly() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getPrincipal() instanceof User) {
                return (User) authentication.getPrincipal();
            }
        } catch (Exception ignored) {
        }
        return null;
    }
}
