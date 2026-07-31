package com.example.Investigation_Tracking_Solution.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FirCreatedEvent {
    private Long firId;
    private String firNumber;
    private String title;
    private Long assignedOfficerUserId;
    private Long triggeredByUserId;
}
