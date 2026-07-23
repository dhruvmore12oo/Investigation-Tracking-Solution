package com.example.Investigation_Tracking_Solution.mapper;

import com.example.Investigation_Tracking_Solution.dto.evidence.EvidenceResponse;
import com.example.Investigation_Tracking_Solution.model.Evidence;

public class EvidenceMapper {

    public static EvidenceResponse toResponse(Evidence evidence) {
        String officerName = null;
        if (evidence.getCollectedBy() != null && evidence.getCollectedBy().getUser() != null) {
            officerName = evidence.getCollectedBy().getUser().getFirstName() + " "
                    + evidence.getCollectedBy().getUser().getLastName();
        }

        Long caseId = null;
        String caseNumber = null;
        if (evidence.getInvestigation() != null && evidence.getInvestigation().getInvestigationCase() != null) {
            caseId = evidence.getInvestigation().getInvestigationCase().getId();
            caseNumber = evidence.getInvestigation().getInvestigationCase().getCaseNumber();
        }

        return EvidenceResponse.builder()
                .id(evidence.getId())
                .evidenceNumber(evidence.getEvidenceNumber())
                .title(evidence.getTitle())
                .description(evidence.getDescription())
                .evidenceType(evidence.getEvidenceType())
                .status(evidence.getStatus())
                .storageLocation(evidence.getStorageLocation())
                .collectedDate(evidence.getCollectedDate())
                .receivedDate(evidence.getReceivedDate())
                .remarks(evidence.getRemarks())
                .investigationId(evidence.getInvestigation() != null ? evidence.getInvestigation().getId() : null)
                .investigationNumber(evidence.getInvestigation() != null ? evidence.getInvestigation().getInvestigationNumber() : null)
                .caseId(caseId)
                .caseNumber(caseNumber)
                .collectedByOfficerId(evidence.getCollectedBy() != null ? evidence.getCollectedBy().getId() : null)
                .collectedByOfficerName(officerName)
                .createdAt(evidence.getCreatedAt())
                .updatedAt(evidence.getUpdatedAt())
                .build();
    }
}
