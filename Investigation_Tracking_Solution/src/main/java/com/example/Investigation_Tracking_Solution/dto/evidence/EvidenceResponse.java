package com.example.Investigation_Tracking_Solution.dto.evidence;

import com.example.Investigation_Tracking_Solution.model.EvidenceStatus;
import com.example.Investigation_Tracking_Solution.model.EvidenceType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class EvidenceResponse {
    private Long id;
    private String evidenceNumber;
    private String title;
    private String description;
    private EvidenceType evidenceType;
    private EvidenceStatus status;
    private String storageLocation;
    private LocalDateTime collectedDate;
    private LocalDateTime receivedDate;
    private String remarks;
    private Long investigationId;
    private String investigationNumber;
    private Long caseId;
    private String caseNumber;
    private Long collectedByOfficerId;
    private String collectedByOfficerName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
