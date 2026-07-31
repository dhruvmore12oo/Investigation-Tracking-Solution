package com.example.Investigation_Tracking_Solution.service.impl;

import com.example.Investigation_Tracking_Solution.annotation.Auditable;
import com.example.Investigation_Tracking_Solution.dto.evidenceattachment.EvidenceAttachmentResponse;
import com.example.Investigation_Tracking_Solution.exception.BadRequestException;
import com.example.Investigation_Tracking_Solution.exception.ResourceNotFoundException;
import com.example.Investigation_Tracking_Solution.mapper.EvidenceAttachmentMapper;
import com.example.Investigation_Tracking_Solution.model.*;
import com.example.Investigation_Tracking_Solution.repository.EvidenceAttachmentRepo;
import com.example.Investigation_Tracking_Solution.repository.EvidenceRepo;
import com.example.Investigation_Tracking_Solution.service.EvidenceAttachmentService;
import com.example.Investigation_Tracking_Solution.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EvidenceAttachmentServiceImpl implements EvidenceAttachmentService {

    private static final long MAX_FILE_SIZE_BYTES = 20 * 1024 * 1024; // 20 MB

    private static final Set<String> ALLOWED_EXTENSIONS = new HashSet<>(Arrays.asList(
            "jpg", "jpeg", "png", "gif", "pdf", "doc", "docx",
            "xls", "xlsx", "csv", "mp4", "avi", "mov", "wav", "mp3"
    ));

    private final EvidenceAttachmentRepo evidenceAttachmentRepo;
    private final EvidenceRepo evidenceRepo;
    private final FileStorageService fileStorageService;

    @Override
    @Transactional
    @Auditable(action = AuditAction.CREATE, module = AuditModule.EVIDENCE, entityType = "EVIDENCE_ATTACHMENT", description = "Uploaded evidence attachment")
    public EvidenceAttachmentResponse uploadAttachment(Long evidenceId, MultipartFile file, User currentUser) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("Uploaded file cannot be empty.");
        }

        if (file.getSize() > MAX_FILE_SIZE_BYTES) {
            throw new BadRequestException("File size exceeds maximum allowed limit of 20MB.");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isBlank()) {
            throw new BadRequestException("File must have a valid name.");
        }

        String extension = getFileExtension(originalFilename);
        if (!ALLOWED_EXTENSIONS.contains(extension.toLowerCase())) {
            throw new BadRequestException("Unsupported file format: '" + extension + "'. Allowed formats: " + ALLOWED_EXTENSIONS);
        }

        Evidence evidence = evidenceRepo.findById(evidenceId)
                .orElseThrow(() -> new ResourceNotFoundException("Could Not Find Evidence With Id : " + evidenceId));

        validateUploadPermissions(evidence, currentUser);

        String storedFileName = UUID.randomUUID().toString() + "_" + originalFilename.replaceAll("\\s+", "_");
        String storagePath = fileStorageService.storeFile(file, storedFileName);

        EvidenceAttachment attachment = EvidenceAttachment.builder()
                .originalFileName(originalFilename)
                .storedFileName(storedFileName)
                .fileExtension(extension)
                .mimeType(file.getContentType() != null ? file.getContentType() : "application/octet-stream")
                .fileSize(file.getSize())
                .storagePath(storagePath)
                .evidence(evidence)
                .uploadedBy(currentUser)
                .build();

        EvidenceAttachment savedAttachment = evidenceAttachmentRepo.save(attachment);
        return EvidenceAttachmentMapper.toResponse(savedAttachment);
    }

    @Override
    @Transactional(readOnly = true)
    public EvidenceAttachmentResponse getAttachmentById(Long attachmentId) {
        EvidenceAttachment attachment = evidenceAttachmentRepo.findById(attachmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Could Not Find Evidence Attachment With Id : " + attachmentId));
        return EvidenceAttachmentMapper.toResponse(attachment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EvidenceAttachmentResponse> getAttachmentsByEvidenceId(Long evidenceId) {
        if (!evidenceRepo.existsById(evidenceId)) {
            throw new ResourceNotFoundException("Could Not Find Evidence With Id : " + evidenceId);
        }
        return evidenceAttachmentRepo.findByEvidence_Id(evidenceId).stream()
                .map(EvidenceAttachmentMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Resource downloadAttachment(Long attachmentId) {
        EvidenceAttachment attachment = evidenceAttachmentRepo.findById(attachmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Could Not Find Evidence Attachment With Id : " + attachmentId));
        return fileStorageService.loadFileAsResource(attachment.getStoragePath());
    }

    @Override
    @Transactional
    @Auditable(action = AuditAction.DELETE, module = AuditModule.EVIDENCE, entityType = "EVIDENCE_ATTACHMENT", description = "Deleted evidence attachment")
    public void deleteAttachment(Long attachmentId, User currentUser) {
        EvidenceAttachment attachment = evidenceAttachmentRepo.findById(attachmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Could Not Find Evidence Attachment With Id : " + attachmentId));

        validateDeletePermissions(attachment, currentUser);

        fileStorageService.deleteFile(attachment.getStoragePath());
        evidenceAttachmentRepo.delete(attachment);
    }

    private void validateUploadPermissions(Evidence evidence, User currentUser) {
        if (currentUser.getRole() == Role.ADMIN || currentUser.getRole() == Role.OFFICER) {
            return;
        }

        if (currentUser.getRole() == Role.INVESTIGATOR) {
            boolean isAssigned = evidence.getInvestigation() != null &&
                    evidence.getInvestigation().getAssignedInvestigator() != null &&
                    evidence.getInvestigation().getAssignedInvestigator().getId().equals(currentUser.getId());
            if (!isAssigned) {
                throw new BadRequestException("Investigators can only upload attachments for investigations assigned to them.");
            }
            return;
        }

        throw new BadRequestException("You do not have permission to upload evidence attachments.");
    }

    private void validateDeletePermissions(EvidenceAttachment attachment, User currentUser) {
        if (currentUser.getRole() == Role.ADMIN) {
            return;
        }

        if (attachment.getUploadedBy() != null && attachment.getUploadedBy().getId().equals(currentUser.getId())) {
            return;
        }

        throw new BadRequestException("You do not have permission to delete this attachment.");
    }

    private String getFileExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < filename.length() - 1) {
            return filename.substring(dotIndex + 1);
        }
        return "";
    }
}
