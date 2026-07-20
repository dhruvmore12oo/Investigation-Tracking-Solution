package com.example.Investigation_Tracking_Solution.service;

import com.example.Investigation_Tracking_Solution.dto.cases.CaseRequest;
import com.example.Investigation_Tracking_Solution.dto.cases.CaseResponse;
import com.example.Investigation_Tracking_Solution.model.CasePriority;
import com.example.Investigation_Tracking_Solution.model.CaseStatus;
import org.springframework.data.domain.Page;

public interface CaseService {
    CaseResponse createCase(CaseRequest request);
    CaseResponse getCaseById(Long id);
    Page<CaseResponse> getAllCases(int page, int size);
    CaseResponse updateCase(Long id, CaseRequest request);
    void deleteCase(Long id);
    CaseResponse getByCaseNumber(String caseNumber);
    Page<CaseResponse> getByStatus(CaseStatus status, int page, int size);
    Page<CaseResponse> getByPriority(CasePriority priority, int page, int size);
    Page<CaseResponse> getByAssignedOfficer(Long officerId, int page, int size);
    Page<CaseResponse> getByFir(Long firId, int page, int size);
    Page<CaseResponse> searchByKeyword(String keyword, int page, int size);
}
