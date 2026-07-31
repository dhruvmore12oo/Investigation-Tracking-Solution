package com.example.Investigation_Tracking_Solution.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvestigationStatusChangedEvent {
    private Long investigationId;
    private String investigationNumber;
    private String oldStatus;
    private String newStatus;
    private Long assignedInvestigatorUserId;
    private Long triggeredByUserId;
}
