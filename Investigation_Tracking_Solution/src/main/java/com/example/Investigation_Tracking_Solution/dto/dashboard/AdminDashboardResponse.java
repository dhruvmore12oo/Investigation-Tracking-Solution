package com.example.Investigation_Tracking_Solution.dto.dashboard;

import com.example.Investigation_Tracking_Solution.dto.auditlog.AuditLogResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminDashboardResponse {
    private long totalUsers;
    private long totalOfficers;
    private long totalInvestigators;
    private long totalCriminals;
    private long totalFirs;
    private long totalCases;
    private long totalInvestigations;
    private long totalEvidence;
    private long totalWitnesses;

    private Map<String, Long> casesByStatus;
    private Map<String, Long> investigationsByStatus;
    private Map<String, Long> evidenceByStatus;
    private Map<String, Long> witnessesByStatus;

    private long unreadNotificationCount;
    private List<AuditLogResponse> recentActivities;
}
