package com.example.Investigation_Tracking_Solution.service.impl;

import com.example.Investigation_Tracking_Solution.dto.search.GlobalSearchItemResponse;
import com.example.Investigation_Tracking_Solution.dto.search.GlobalSearchResponse;
import com.example.Investigation_Tracking_Solution.exception.BadRequestException;
import com.example.Investigation_Tracking_Solution.model.*;
import com.example.Investigation_Tracking_Solution.repository.*;
import com.example.Investigation_Tracking_Solution.service.GlobalSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GlobalSearchServiceImpl implements GlobalSearchService {

    private final UserRepo userRepository;
    private final OfficerRepo officerRepository;
    private final CriminalRepo criminalRepository;
    private final FirRepo firRepository;
    private final CaseRepo caseRepository;
    private final InvestigationRepo investigationRepository;
    private final EvidenceRepo evidenceRepository;
    private final WitnessRepo witnessRepository;
    private final InvestigationNoteRepo investigationNoteRepository;
    private final NotificationRepo notificationRepository;

    @Override
    @Transactional(readOnly = true)
    public GlobalSearchResponse globalSearch(String keyword, String moduleFilter, int page, int size, User currentUser) {
        if (keyword == null || keyword.trim().length() < 2 || keyword.trim().length() > 100) {
            throw new BadRequestException("Keyword must be between 2 and 100 characters.");
        }

        String cleanKeyword = keyword.trim();
        Pageable pageable = PageRequest.of(page, Math.min(size, 50));
        List<GlobalSearchItemResponse> results = new ArrayList<>();
        String filter = moduleFilter != null ? moduleFilter.trim().toUpperCase() : null;

        Role userRole = currentUser.getRole();

        // 1. USER module (ADMIN only)
        if (userRole == Role.ADMIN && (filter == null || filter.equals("USER"))) {
            userRepository.searchByKeyword(cleanKeyword, pageable).forEach(u ->
                results.add(GlobalSearchItemResponse.builder()
                        .module("USER")
                        .entityId(u.getId())
                        .title(u.getFirstName() + " " + u.getLastName())
                        .subtitle(u.getRole() != null ? u.getRole().name() : "USER")
                        .description(u.getEmail())
                        .referenceNumber(null)
                        .build())
            );
        }

        // 2. OFFICER module (ADMIN, OFFICER)
        if ((userRole == Role.ADMIN || userRole == Role.OFFICER) && (filter == null || filter.equals("OFFICER"))) {
            officerRepository.searchByKeyword(cleanKeyword, pageable).forEach(o -> {
                String officerName = o.getUser() != null ? o.getUser().getFirstName() + " " + o.getUser().getLastName() : "Badge #" + o.getBadgeNumber();
                results.add(GlobalSearchItemResponse.builder()
                        .module("OFFICER")
                        .entityId(o.getId())
                        .title(officerName)
                        .subtitle(o.getDepartment())
                        .description("Badge: " + o.getBadgeNumber() + " | Dept: " + o.getDepartment())
                        .referenceNumber(o.getBadgeNumber())
                        .build());
            });
        }

        // 3. CRIMINAL module (All roles)
        if (filter == null || filter.equals("CRIMINAL")) {
            criminalRepository.searchByKeyword(cleanKeyword, pageable).forEach(c ->
                results.add(GlobalSearchItemResponse.builder()
                        .module("CRIMINAL")
                        .entityId(c.getId())
                        .title(c.getFirstName() + " " + c.getLastName())
                        .subtitle(c.getRiskLevel() != null ? c.getRiskLevel().name() : "CRIMINAL")
                        .description(c.getCity() != null ? "City: " + c.getCity() : c.getAddress())
                        .referenceNumber(c.getAadhaarNumber())
                        .build())
            );
        }

        // 4. FIR module (ADMIN, OFFICER)
        if ((userRole == Role.ADMIN || userRole == Role.OFFICER) && (filter == null || filter.equals("FIR"))) {
            firRepository.searchByKeyword(cleanKeyword, pageable).forEach(f ->
                results.add(GlobalSearchItemResponse.builder()
                        .module("FIR")
                        .entityId(f.getId())
                        .title(f.getTitle())
                        .subtitle(f.getCrimeType())
                        .description(f.getDescription())
                        .referenceNumber(f.getFirNumber())
                        .build())
            );
        }

        // 5. CASE module (All roles, scoped for INVESTIGATOR)
        if (filter == null || filter.equals("CASE")) {
            caseRepository.searchByKeyword(cleanKeyword, pageable).forEach(c -> {
                if (userRole != Role.INVESTIGATOR || (c.getAssignedInvestigator() != null && c.getAssignedInvestigator().getId().equals(currentUser.getId()))) {
                    results.add(GlobalSearchItemResponse.builder()
                            .module("CASE")
                            .entityId(c.getId())
                            .title(c.getTitle())
                            .subtitle(c.getStatus() != null ? c.getStatus().name() : "CASE")
                            .description(c.getDescription())
                            .referenceNumber(c.getCaseNumber())
                            .build());
                }
            });
        }

        // 6. INVESTIGATION module (All roles, scoped for INVESTIGATOR)
        if (filter == null || filter.equals("INVESTIGATION")) {
            investigationRepository.searchByKeyword(cleanKeyword, pageable).forEach(i -> {
                if (userRole != Role.INVESTIGATOR || (i.getAssignedInvestigator() != null && i.getAssignedInvestigator().getId().equals(currentUser.getId()))) {
                    results.add(GlobalSearchItemResponse.builder()
                            .module("INVESTIGATION")
                            .entityId(i.getId())
                            .title(i.getInvestigationNumber())
                            .subtitle(i.getStatus() != null ? i.getStatus().name() : "INVESTIGATION")
                            .description(i.getSummary())
                            .referenceNumber(i.getInvestigationNumber())
                            .build());
                }
            });
        }

        // 7. EVIDENCE module (All roles)
        if (filter == null || filter.equals("EVIDENCE")) {
            evidenceRepository.searchByKeyword(cleanKeyword, pageable).forEach(e ->
                results.add(GlobalSearchItemResponse.builder()
                        .module("EVIDENCE")
                        .entityId(e.getId())
                        .title(e.getTitle())
                        .subtitle(e.getEvidenceType() != null ? e.getEvidenceType().name() : "EVIDENCE")
                        .description(e.getDescription())
                        .referenceNumber(e.getEvidenceNumber())
                        .build())
            );
        }

        // 8. WITNESS module (All roles)
        if (filter == null || filter.equals("WITNESS")) {
            witnessRepository.searchByKeyword(cleanKeyword, pageable).forEach(w ->
                results.add(GlobalSearchItemResponse.builder()
                        .module("WITNESS")
                        .entityId(w.getId())
                        .title(w.getFirstName() + " " + w.getLastName())
                        .subtitle(w.getWitnessStatus() != null ? w.getWitnessStatus().name() : "WITNESS")
                        .description(w.getStatement())
                        .referenceNumber(w.getWitnessNumber())
                        .build())
            );
        }

        // 9. INVESTIGATION NOTE module (All roles)
        if (filter == null || filter.equals("INVESTIGATION_NOTE") || filter.equals("NOTE")) {
            investigationNoteRepository.searchByKeyword(cleanKeyword, pageable).forEach(n ->
                results.add(GlobalSearchItemResponse.builder()
                        .module("INVESTIGATION_NOTE")
                        .entityId(n.getId())
                        .title(n.getTitle())
                        .subtitle(n.getNoteType() != null ? n.getNoteType().name() : "NOTE")
                        .description(n.getNote())
                        .referenceNumber(null)
                        .build())
            );
        }

        // 10. NOTIFICATION module (User's own notifications for non-admin, or search for admin)
        if (filter == null || filter.equals("NOTIFICATION")) {
            if (userRole == Role.ADMIN) {
                notificationRepository.searchByKeyword(cleanKeyword, pageable).forEach(n ->
                    results.add(GlobalSearchItemResponse.builder()
                            .module("NOTIFICATION")
                            .entityId(n.getId())
                            .title(n.getTitle())
                            .subtitle(n.getNotificationType() != null ? n.getNotificationType().name() : "NOTIFICATION")
                            .description(n.getMessage())
                            .referenceNumber(null)
                            .build())
                );
            } else {
                notificationRepository.searchByRecipientAndKeyword(currentUser.getId(), cleanKeyword, pageable).forEach(n ->
                    results.add(GlobalSearchItemResponse.builder()
                            .module("NOTIFICATION")
                            .entityId(n.getId())
                            .title(n.getTitle())
                            .subtitle(n.getNotificationType() != null ? n.getNotificationType().name() : "NOTIFICATION")
                            .description(n.getMessage())
                            .referenceNumber(null)
                            .build())
                );
            }
        }

        return GlobalSearchResponse.builder()
                .totalResults(results.size())
                .results(results)
                .build();
    }
}
