package com.example.Investigation_Tracking_Solution.dto.cases;

import com.example.Investigation_Tracking_Solution.model.CasePriority;
import com.example.Investigation_Tracking_Solution.model.CaseStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CaseRequest {

    @NotBlank(message = "Case number is required")
    @Size(max = 50, message = "Case number cannot exceed 50 characters")
    private String caseNumber;

    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title cannot exceed 100 characters")
    private String title;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    @NotNull(message = "Priority is required")
    private CasePriority priority;

    @NotNull(message = "Status is required")
    private CaseStatus status;

    @NotNull(message = "FIR ID is required")
    private Long firId;

    private Long assignedOfficerId;

    private Long assignedInvestigatorId;

    private List<Long> criminalIds;
}
