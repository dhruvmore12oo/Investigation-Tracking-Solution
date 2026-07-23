package com.example.Investigation_Tracking_Solution.dto.investigation;

import com.example.Investigation_Tracking_Solution.model.CasePriority;
import com.example.Investigation_Tracking_Solution.model.InvestigationStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class InvestigationRequest {

    @NotBlank(message = "Investigation number is required")
    @Size(max = 50, message = "Investigation number cannot exceed 50 characters")
    private String investigationNumber;

    @NotNull(message = "Case ID is required")
    private Long caseId;

    @NotNull(message = "Assigned investigator ID is required")
    private Long assignedInvestigatorId;

    @NotNull(message = "Investigation status is required")
    private InvestigationStatus status;

    @NotNull(message = "Priority is required")
    private CasePriority priority;

    @NotNull(message = "Start date is required")
    private LocalDateTime startDate;

    private LocalDateTime expectedCompletionDate;

    private LocalDateTime completedDate;

    @Size(max = 2000, message = "Summary cannot exceed 2000 characters")
    private String summary;

    @Size(max = 1000, message = "Remarks cannot exceed 1000 characters")
    private String remarks;
}
