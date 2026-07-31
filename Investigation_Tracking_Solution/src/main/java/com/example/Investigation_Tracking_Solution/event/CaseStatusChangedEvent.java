package com.example.Investigation_Tracking_Solution.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CaseStatusChangedEvent {
    private Long caseId;
    private String caseNumber;
    private String oldStatus;
    private String newStatus;
    private Long assignedOfficerUserId;
    private Long assignedInvestigatorUserId;
    private Long triggeredByUserId;
}
