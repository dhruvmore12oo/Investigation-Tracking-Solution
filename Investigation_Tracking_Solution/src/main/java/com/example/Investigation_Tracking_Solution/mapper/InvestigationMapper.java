package com.example.Investigation_Tracking_Solution.mapper;

import com.example.Investigation_Tracking_Solution.dto.investigation.InvestigationResponse;
import com.example.Investigation_Tracking_Solution.model.Investigation;

public class InvestigationMapper {

    public static InvestigationResponse toResponse(Investigation investigation) {
        String investigatorName = null;
        if (investigation.getAssignedInvestigator() != null) {
            investigatorName = investigation.getAssignedInvestigator().getFirstName() + " "
                    + investigation.getAssignedInvestigator().getLastName();
        }

        return InvestigationResponse.builder()
                .id(investigation.getId())
                .investigationNumber(investigation.getInvestigationNumber())
                .caseId(investigation.getInvestigationCase() != null ? investigation.getInvestigationCase().getId() : null)
                .caseNumber(investigation.getInvestigationCase() != null ? investigation.getInvestigationCase().getCaseNumber() : null)
                .assignedInvestigatorId(investigation.getAssignedInvestigator() != null ? investigation.getAssignedInvestigator().getId() : null)
                .assignedInvestigatorName(investigatorName)
                .status(investigation.getStatus())
                .priority(investigation.getPriority())
                .startDate(investigation.getStartedAt())
                .expectedCompletionDate(investigation.getExpectedCompletionDate())
                .completedDate(investigation.getCompletedDate())
                .closedAt(investigation.getClosedAt())
                .summary(investigation.getSummary())
                .remarks(investigation.getRemarks())
                .createdAt(investigation.getCreatedAt())
                .updatedAt(investigation.getUpdatedAt())
                .build();
    }
}
