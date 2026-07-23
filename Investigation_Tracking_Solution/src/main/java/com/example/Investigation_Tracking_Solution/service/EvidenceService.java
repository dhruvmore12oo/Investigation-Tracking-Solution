package com.example.Investigation_Tracking_Solution.service;

import com.example.Investigation_Tracking_Solution.dto.evidence.EvidenceRequest;
import com.example.Investigation_Tracking_Solution.dto.evidence.EvidenceResponse;
import com.example.Investigation_Tracking_Solution.model.EvidenceStatus;
import com.example.Investigation_Tracking_Solution.model.EvidenceType;
import org.springframework.data.domain.Page;

public interface EvidenceService {
    EvidenceResponse createEvidence(EvidenceRequest request);
    EvidenceResponse getEvidenceById(Long id);
    EvidenceResponse getByEvidenceNumber(String evidenceNumber);
    Page<EvidenceResponse> getAllEvidence(int page, int size);
    EvidenceResponse updateEvidence(Long id, EvidenceRequest request);
    void deleteEvidence(Long id);
    Page<EvidenceResponse> getByInvestigation(Long investigationId, int page, int size);
    Page<EvidenceResponse> getByType(EvidenceType type, int page, int size);
    Page<EvidenceResponse> getByStatus(EvidenceStatus status, int page, int size);
    Page<EvidenceResponse> searchByKeyword(String keyword, int page, int size);
}
