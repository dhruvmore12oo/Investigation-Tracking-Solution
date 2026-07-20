package com.example.Investigation_Tracking_Solution.mapper;

import com.example.Investigation_Tracking_Solution.dto.cases.CaseResponse;
import com.example.Investigation_Tracking_Solution.model.Case;

import java.util.List;

public class CaseMapper {
    public static CaseResponse toResponse(Case caseEntity, List<Long> criminalIds) {
        String officerName = null;
        if (caseEntity.getAssignedOfficer() != null && caseEntity.getAssignedOfficer().getUser() != null) {
            officerName = caseEntity.getAssignedOfficer().getUser().getFirstName() + " " + caseEntity.getAssignedOfficer().getUser().getLastName();
        }

        String investigatorName = null;
        if (caseEntity.getAssignedInvestigator() != null) {
            investigatorName = caseEntity.getAssignedInvestigator().getFirstName() + " " + caseEntity.getAssignedInvestigator().getLastName();
        }

        return CaseResponse.builder()
                .id(caseEntity.getId())
                .caseNumber(caseEntity.getCaseNumber())
                .title(caseEntity.getTitle())
                .description(caseEntity.getDescription())
                .priority(caseEntity.getPriority())
                .status(caseEntity.getStatus())
                .firId(caseEntity.getFir() != null ? caseEntity.getFir().getId() : null)
                .firNumber(caseEntity.getFir() != null ? caseEntity.getFir().getFirNumber() : null)
                .assignedOfficerId(caseEntity.getAssignedOfficer() != null ? caseEntity.getAssignedOfficer().getId() : null)
                .officerName(officerName)
                .assignedInvestigatorId(caseEntity.getAssignedInvestigator() != null ? caseEntity.getAssignedInvestigator().getId() : null)
                .investigatorName(investigatorName)
                .criminalIds(criminalIds)
                .createdAt(caseEntity.getCreatedAt())
                .build();
    }
}
