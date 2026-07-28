package com.example.Investigation_Tracking_Solution.dto.evidenceattachment;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class EvidenceAttachmentResponse {
    private Long id;
    private Long attachmentId;
    private String originalFileName;
    private String storedFileName;
    private String fileExtension;
    private String mimeType;
    private Long fileSize;
    private String storagePath;
    private LocalDateTime uploadedAt;
    private Long uploadedByUserId;
    private String uploadedByName;
    private Long evidenceId;
    private String evidenceNumber;
}
