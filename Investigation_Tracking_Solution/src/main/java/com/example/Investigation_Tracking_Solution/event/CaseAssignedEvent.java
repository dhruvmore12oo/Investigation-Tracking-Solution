package com.example.Investigation_Tracking_Solution.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CaseAssignedEvent {
    private Long caseId;
    private String caseNumber;
    private Long assignedOfficerUserId;
    private Long assignedInvestigatorUserId;
    private Long triggeredByUserId;
}
