package com.example.Investigation_Tracking_Solution.service;

import com.example.Investigation_Tracking_Solution.dto.evidenceattachment.EvidenceAttachmentResponse;
import com.example.Investigation_Tracking_Solution.model.User;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface EvidenceAttachmentService {
    EvidenceAttachmentResponse uploadAttachment(Long evidenceId, MultipartFile file, User currentUser);
    EvidenceAttachmentResponse getAttachmentById(Long attachmentId);
    List<EvidenceAttachmentResponse> getAttachmentsByEvidenceId(Long evidenceId);
    Resource downloadAttachment(Long attachmentId);
    void deleteAttachment(Long attachmentId, User currentUser);
}
