package com.example.Investigation_Tracking_Solution.service;

import com.example.Investigation_Tracking_Solution.dto.auditlog.AuditLogResponse;
import com.example.Investigation_Tracking_Solution.dto.dashboard.*;

import java.util.List;

public interface DashboardService {
    AdminDashboardResponse getAdminDashboard();
    OfficerDashboardResponse getOfficerDashboard(Long officerUserId);
    InvestigatorDashboardResponse getInvestigatorDashboard(Long investigatorUserId);
    List<MonthlyCountResponse> getMonthlyCases();
    List<MonthlyCountResponse> getMonthlyFirs();
    List<MonthlyCountResponse> getMonthlyInvestigations();
    List<GroupCountResponse> getEvidenceByType();
    List<GroupCountResponse> getEvidenceByStatus();
    List<GroupCountResponse> getWitnessByStatus();
    List<GroupCountResponse> getCriminalsByCity();
    List<AuditLogResponse> getRecentActivities();
}
