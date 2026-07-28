package com.example.Investigation_Tracking_Solution.mapper;

import com.example.Investigation_Tracking_Solution.dto.evidenceattachment.EvidenceAttachmentResponse;
import com.example.Investigation_Tracking_Solution.model.EvidenceAttachment;

public class EvidenceAttachmentMapper {

    public static EvidenceAttachmentResponse toResponse(EvidenceAttachment attachment) {
        String uploaderName = null;
        if (attachment.getUploadedBy() != null) {
            uploaderName = attachment.getUploadedBy().getFirstName() + " "
                    + attachment.getUploadedBy().getLastName();
        }

        return EvidenceAttachmentResponse.builder()
                .id(attachment.getId())
                .attachmentId(attachment.getId())
                .originalFileName(attachment.getOriginalFileName())
                .storedFileName(attachment.getStoredFileName())
                .fileExtension(attachment.getFileExtension())
                .mimeType(attachment.getMimeType())
                .fileSize(attachment.getFileSize())
                .storagePath(attachment.getStoragePath())
                .uploadedAt(attachment.getUploadedAt())
                .uploadedByUserId(attachment.getUploadedBy() != null ? attachment.getUploadedBy().getId() : null)
                .uploadedByName(uploaderName)
                .evidenceId(attachment.getEvidence() != null ? attachment.getEvidence().getId() : null)
                .evidenceNumber(attachment.getEvidence() != null ? attachment.getEvidence().getEvidenceNumber() : null)
                .build();
    }
}
