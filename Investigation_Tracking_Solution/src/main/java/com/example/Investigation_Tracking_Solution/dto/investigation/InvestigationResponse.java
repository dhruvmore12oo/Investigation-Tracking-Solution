package com.example.Investigation_Tracking_Solution.dto.investigation;

import com.example.Investigation_Tracking_Solution.model.CasePriority;
import com.example.Investigation_Tracking_Solution.model.InvestigationStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class InvestigationResponse {
    private Long id;
    private String investigationNumber;
    private Long caseId;
    private String caseNumber;
    private Long assignedInvestigatorId;
    private String assignedInvestigatorName;
    private InvestigationStatus status;
    private CasePriority priority;
    private LocalDateTime startDate;
    private LocalDateTime expectedCompletionDate;
    private LocalDateTime completedDate;
    private LocalDateTime closedAt;
    private String summary;
    private String remarks;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
