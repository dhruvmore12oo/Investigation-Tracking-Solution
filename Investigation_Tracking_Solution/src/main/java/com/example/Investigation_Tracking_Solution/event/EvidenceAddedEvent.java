package com.example.Investigation_Tracking_Solution.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EvidenceAddedEvent {
    private Long evidenceId;
    private String evidenceNumber;
    private String title;
    private Long investigationId;
    private String investigationNumber;
    private Long assignedInvestigatorUserId;
    private Long collectedByOfficerUserId;
    private Long triggeredByUserId;
}
