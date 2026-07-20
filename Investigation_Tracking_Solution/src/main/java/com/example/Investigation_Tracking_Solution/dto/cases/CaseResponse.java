package com.example.Investigation_Tracking_Solution.dto.cases;

import com.example.Investigation_Tracking_Solution.model.CasePriority;
import com.example.Investigation_Tracking_Solution.model.CaseStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class CaseResponse {
    private Long id;
    private String caseNumber;
    private String title;
    private String description;
    private CasePriority priority;
    private CaseStatus status;
    private Long firId;
    private String firNumber;
    private Long assignedOfficerId;
    private String officerName;
    private Long assignedInvestigatorId;
    private String investigatorName;
    private List<Long> criminalIds;
    private LocalDateTime createdAt;
}
