package com.example.Investigation_Tracking_Solution.service;

import com.example.Investigation_Tracking_Solution.dto.investigation.InvestigationRequest;
import com.example.Investigation_Tracking_Solution.dto.investigation.InvestigationResponse;
import com.example.Investigation_Tracking_Solution.model.CasePriority;
import com.example.Investigation_Tracking_Solution.model.InvestigationStatus;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

public interface InvestigationService {
    InvestigationResponse createInvestigation(InvestigationRequest request);
    InvestigationResponse getInvestigationById(Long id);
    InvestigationResponse getByInvestigationNumber(String investigationNumber);
    Page<InvestigationResponse> getAllInvestigations(int page, int size);
    InvestigationResponse updateInvestigation(Long id, InvestigationRequest request);
    void deleteInvestigation(Long id);
    Page<InvestigationResponse> getByStatus(InvestigationStatus status, int page, int size);
    Page<InvestigationResponse> getByPriority(CasePriority priority, int page, int size);
    Page<InvestigationResponse> getByInvestigator(Long investigatorId, int page, int size);
    Page<InvestigationResponse> getByCase(Long caseId, int page, int size);
    Page<InvestigationResponse> getByDateRange(LocalDateTime startDate, LocalDateTime endDate, int page, int size);
    Page<InvestigationResponse> searchByKeyword(String keyword, int page, int size);
}
