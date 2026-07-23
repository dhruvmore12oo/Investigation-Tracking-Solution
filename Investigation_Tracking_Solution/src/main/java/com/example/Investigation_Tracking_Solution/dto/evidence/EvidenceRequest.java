package com.example.Investigation_Tracking_Solution.dto.evidence;

import com.example.Investigation_Tracking_Solution.model.EvidenceStatus;
import com.example.Investigation_Tracking_Solution.model.EvidenceType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class EvidenceRequest {

    @NotBlank(message = "Evidence number is required")
    @Size(max = 50, message = "Evidence number cannot exceed 50 characters")
    private String evidenceNumber;

    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title cannot exceed 100 characters")
    private String title;

    @Size(max = 2000, message = "Description cannot exceed 2000 characters")
    private String description;

    @NotNull(message = "Evidence type is required")
    private EvidenceType evidenceType;

    @NotNull(message = "Evidence status is required")
    private EvidenceStatus status;

    @Size(max = 255, message = "Storage location cannot exceed 255 characters")
    private String storageLocation;

    @NotNull(message = "Collected date is required")
    @PastOrPresent(message = "Collected date cannot be in the future")
    private LocalDateTime collectedDate;

    @PastOrPresent(message = "Received date cannot be in the future")
    private LocalDateTime receivedDate;

    @Size(max = 1000, message = "Remarks cannot exceed 1000 characters")
    private String remarks;

    @NotNull(message = "Investigation ID is required")
    private Long investigationId;

    @NotNull(message = "Collecting officer ID is required")
    private Long collectedByOfficerId;
}
