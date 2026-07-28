package com.example.Investigation_Tracking_Solution.controller;

import com.example.Investigation_Tracking_Solution.dto.auditlog.AuditLogResponse;
import com.example.Investigation_Tracking_Solution.dto.dashboard.*;
import com.example.Investigation_Tracking_Solution.model.User;
import com.example.Investigation_Tracking_Solution.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminDashboardResponse> getAdminDashboard() {
        return ResponseEntity.ok(dashboardService.getAdminDashboard());
    }

    @GetMapping("/officer")
    @PreAuthorize("hasAnyRole('ADMIN', 'OFFICER')")
    public ResponseEntity<OfficerDashboardResponse> getOfficerDashboard(@AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(dashboardService.getOfficerDashboard(currentUser.getId()));
    }

    @GetMapping("/investigator")
    @PreAuthorize("hasAnyRole('ADMIN', 'INVESTIGATOR')")
    public ResponseEntity<InvestigatorDashboardResponse> getInvestigatorDashboard(@AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(dashboardService.getInvestigatorDashboard(currentUser.getId()));
    }

    @GetMapping("/cases/monthly")
    @PreAuthorize("hasAnyRole('ADMIN', 'OFFICER', 'INVESTIGATOR')")
    public ResponseEntity<List<MonthlyCountResponse>> getMonthlyCases() {
        return ResponseEntity.ok(dashboardService.getMonthlyCases());
    }

    @GetMapping("/firs/monthly")
    @PreAuthorize("hasAnyRole('ADMIN', 'OFFICER', 'INVESTIGATOR')")
    public ResponseEntity<List<MonthlyCountResponse>> getMonthlyFirs() {
        return ResponseEntity.ok(dashboardService.getMonthlyFirs());
    }

    @GetMapping("/investigations/monthly")
    @PreAuthorize("hasAnyRole('ADMIN', 'OFFICER', 'INVESTIGATOR')")
    public ResponseEntity<List<MonthlyCountResponse>> getMonthlyInvestigations() {
        return ResponseEntity.ok(dashboardService.getMonthlyInvestigations());
    }

    @GetMapping("/evidence/type")
    @PreAuthorize("hasAnyRole('ADMIN', 'OFFICER', 'INVESTIGATOR')")
    public ResponseEntity<List<GroupCountResponse>> getEvidenceByType() {
        return ResponseEntity.ok(dashboardService.getEvidenceByType());
    }

    @GetMapping("/evidence/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'OFFICER', 'INVESTIGATOR')")
    public ResponseEntity<List<GroupCountResponse>> getEvidenceByStatus() {
        return ResponseEntity.ok(dashboardService.getEvidenceByStatus());
    }

    @GetMapping("/witness/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'OFFICER', 'INVESTIGATOR')")
    public ResponseEntity<List<GroupCountResponse>> getWitnessByStatus() {
        return ResponseEntity.ok(dashboardService.getWitnessByStatus());
    }

    @GetMapping("/criminals/city")
    @PreAuthorize("hasAnyRole('ADMIN', 'OFFICER', 'INVESTIGATOR')")
    public ResponseEntity<List<GroupCountResponse>> getCriminalsByCity() {
        return ResponseEntity.ok(dashboardService.getCriminalsByCity());
    }

    @GetMapping("/recent-activities")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AuditLogResponse>> getRecentActivities() {
        return ResponseEntity.ok(dashboardService.getRecentActivities());
    }
}
