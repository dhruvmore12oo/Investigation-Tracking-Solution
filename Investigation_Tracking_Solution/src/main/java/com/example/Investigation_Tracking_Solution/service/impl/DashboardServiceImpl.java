package com.example.Investigation_Tracking_Solution.service.impl;

import com.example.Investigation_Tracking_Solution.dto.auditlog.AuditLogResponse;
import com.example.Investigation_Tracking_Solution.dto.dashboard.*;
import com.example.Investigation_Tracking_Solution.mapper.AuditLogMapper;
import com.example.Investigation_Tracking_Solution.mapper.CaseMapper;
import com.example.Investigation_Tracking_Solution.mapper.FirMapper;
import com.example.Investigation_Tracking_Solution.mapper.InvestigationNoteMapper;
import com.example.Investigation_Tracking_Solution.model.*;
import com.example.Investigation_Tracking_Solution.repository.*;
import com.example.Investigation_Tracking_Solution.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final UserRepo userRepository;
    private final CriminalRepo criminalRepository;
    private final FirRepo firRepository;
    private final CaseRepo caseRepository;
    private final InvestigationRepo investigationRepository;
    private final EvidenceRepo evidenceRepository;
    private final WitnessRepo witnessRepository;
    private final InvestigationNoteRepo investigationNoteRepository;
    private final NotificationRepo notificationRepository;
    private final AuditLogRepo auditLogRepository;

    @Override
    public AdminDashboardResponse getAdminDashboard() {
        long totalUsers = userRepository.count();
        long totalOfficers = userRepository.countByRole(Role.OFFICER);
        long totalInvestigators = userRepository.countByRole(Role.INVESTIGATOR);
        long totalCriminals = criminalRepository.count();
        long totalFirs = firRepository.count();
        long totalCases = caseRepository.count();
        long totalInvestigations = investigationRepository.count();
        long totalEvidence = evidenceRepository.count();
        long totalWitnesses = witnessRepository.count();

        Map<String, Long> casesByStatus = mapGroupResults(caseRepository.findCaseCountsByStatus());
        Map<String, Long> investigationsByStatus = mapGroupResults(investigationRepository.findInvestigationCountsByStatus());
        Map<String, Long> evidenceByStatus = mapGroupResults(evidenceRepository.findEvidenceCountsByStatus());
        Map<String, Long> witnessesByStatus = mapGroupResults(witnessRepository.findWitnessCountsByStatus());

        long unreadNotifications = notificationRepository.count();

        List<AuditLogResponse> recentActivities = auditLogRepository.findAll(
                PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "createdAt"))
        ).map(AuditLogMapper::toResponse).getContent();

        return AdminDashboardResponse.builder()
                .totalUsers(totalUsers)
                .totalOfficers(totalOfficers)
                .totalInvestigators(totalInvestigators)
                .totalCriminals(totalCriminals)
                .totalFirs(totalFirs)
                .totalCases(totalCases)
                .totalInvestigations(totalInvestigations)
                .totalEvidence(totalEvidence)
                .totalWitnesses(totalWitnesses)
                .casesByStatus(casesByStatus)
                .investigationsByStatus(investigationsByStatus)
                .evidenceByStatus(evidenceByStatus)
                .witnessesByStatus(witnessesByStatus)
                .unreadNotificationCount(unreadNotifications)
                .recentActivities(recentActivities)
                .build();
    }

    @Override
    public OfficerDashboardResponse getOfficerDashboard(Long officerUserId) {
        long assignedFirCount = firRepository.countByOfficerId(officerUserId);
        long assignedCaseCount = caseRepository.countByAssignedOfficer_Id(officerUserId);
        long evidenceCollectedCount = evidenceRepository.countByCollectedBy_Id(officerUserId);

        var recentFirs = firRepository.findByOfficerId(officerUserId, PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "createdAt")))
                .map(FirMapper::toResponse).getContent();

        var recentCases = caseRepository.findByAssignedOfficer_Id(officerUserId, PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "createdAt")))
                .map(c -> CaseMapper.toResponse(c, java.util.Collections.emptyList())).getContent();

        long unreadNotifications = notificationRepository.countByRecipient_IdAndIsReadFalse(officerUserId);

        return OfficerDashboardResponse.builder()
                .assignedFirCount(assignedFirCount)
                .assignedCaseCount(assignedCaseCount)
                .evidenceCollectedCount(evidenceCollectedCount)
                .recentFirs(recentFirs)
                .recentCases(recentCases)
                .pendingWorkCount(assignedCaseCount)
                .unreadNotificationCount(unreadNotifications)
                .build();
    }

    @Override
    public InvestigatorDashboardResponse getInvestigatorDashboard(Long investigatorUserId) {
        long assignedInvestigationsCount = investigationRepository.countByAssignedInvestigator_Id(investigatorUserId);
        long completedInvestigationsCount = investigationRepository.countByAssignedInvestigator_IdAndStatus(investigatorUserId, InvestigationStatus.COMPLETED);
        long pendingInvestigationsCount = investigationRepository.countByAssignedInvestigator_IdAndStatus(investigatorUserId, InvestigationStatus.OPEN)
                + investigationRepository.countByAssignedInvestigator_IdAndStatus(investigatorUserId, InvestigationStatus.IN_PROGRESS)
                + investigationRepository.countByAssignedInvestigator_IdAndStatus(investigatorUserId, InvestigationStatus.ON_HOLD);

        long evidenceCount = evidenceRepository.countByAssignedInvestigator_Id(investigatorUserId);
        long witnessCount = witnessRepository.countByAssignedInvestigator_Id(investigatorUserId);

        var recentNotes = investigationNoteRepository.findByCreatedBy_Id(investigatorUserId, PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "createdAt")))
                .map(InvestigationNoteMapper::toResponse).getContent();

        long unreadNotifications = notificationRepository.countByRecipient_IdAndIsReadFalse(investigatorUserId);

        return InvestigatorDashboardResponse.builder()
                .assignedInvestigationsCount(assignedInvestigationsCount)
                .completedInvestigationsCount(completedInvestigationsCount)
                .pendingInvestigationsCount(pendingInvestigationsCount)
                .evidenceInAssignedInvestigationsCount(evidenceCount)
                .witnessesInAssignedCasesCount(witnessCount)
                .recentInvestigationNotes(recentNotes)
                .unreadNotificationCount(unreadNotifications)
                .build();
    }

    @Override
    public List<MonthlyCountResponse> getMonthlyCases() {
        return mapMonthlyResults(caseRepository.findMonthlyCaseCounts());
    }

    @Override
    public List<MonthlyCountResponse> getMonthlyFirs() {
        return mapMonthlyResults(firRepository.findMonthlyFirCounts());
    }

    @Override
    public List<MonthlyCountResponse> getMonthlyInvestigations() {
        return mapMonthlyResults(investigationRepository.findMonthlyInvestigationCounts());
    }

    @Override
    public List<GroupCountResponse> getEvidenceByType() {
        return mapGroupResponseList(evidenceRepository.findEvidenceCountsByType());
    }

    @Override
    public List<GroupCountResponse> getEvidenceByStatus() {
        return mapGroupResponseList(evidenceRepository.findEvidenceCountsByStatus());
    }

    @Override
    public List<GroupCountResponse> getWitnessByStatus() {
        return mapGroupResponseList(witnessRepository.findWitnessCountsByStatus());
    }

    @Override
    public List<GroupCountResponse> getCriminalsByCity() {
        return mapGroupResponseList(criminalRepository.findCriminalCountsByCity());
    }

    @Override
    public List<AuditLogResponse> getRecentActivities() {
        return auditLogRepository.findAll(PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt")))
                .map(AuditLogMapper::toResponse).getContent();
    }

    private Map<String, Long> mapGroupResults(List<Object[]> queryResults) {
        Map<String, Long> map = new HashMap<>();
        if (queryResults != null) {
            for (Object[] row : queryResults) {
                if (row.length >= 2 && row[0] != null) {
                    map.put(row[0].toString(), ((Number) row[1]).longValue());
                }
            }
        }
        return map;
    }

    private List<GroupCountResponse> mapGroupResponseList(List<Object[]> queryResults) {
        if (queryResults == null) return Collections.emptyList();
        return queryResults.stream()
                .filter(row -> row.length >= 2 && row[0] != null)
                .map(row -> new GroupCountResponse(row[0].toString(), ((Number) row[1]).longValue()))
                .collect(Collectors.toList());
    }

    private List<MonthlyCountResponse> mapMonthlyResults(List<Object[]> queryResults) {
        if (queryResults == null) return Collections.emptyList();
        return queryResults.stream()
                .filter(row -> row.length >= 3 && row[0] != null && row[1] != null)
                .map(row -> new MonthlyCountResponse(((Number) row[0]).intValue(), ((Number) row[1]).intValue(), ((Number) row[2]).longValue()))
                .collect(Collectors.toList());
    }
}
