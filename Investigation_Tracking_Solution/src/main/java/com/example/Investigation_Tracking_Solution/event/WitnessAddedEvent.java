package com.example.Investigation_Tracking_Solution.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WitnessAddedEvent {
    private Long witnessId;
    private String witnessNumber;
    private Long caseId;
    private String caseNumber;
    private Long assignedInvestigatorUserId;
    private Long assignedOfficerUserId;
    private boolean isProtected;
    private Long triggeredByUserId;
}
