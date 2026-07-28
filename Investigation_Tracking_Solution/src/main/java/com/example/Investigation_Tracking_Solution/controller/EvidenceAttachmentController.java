package com.example.Investigation_Tracking_Solution.controller;

import com.example.Investigation_Tracking_Solution.dto.evidenceattachment.EvidenceAttachmentResponse;
import com.example.Investigation_Tracking_Solution.model.User;
import com.example.Investigation_Tracking_Solution.service.EvidenceAttachmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/evidence-attachments")
@RequiredArgsConstructor
public class EvidenceAttachmentController {

    private final EvidenceAttachmentService evidenceAttachmentService;

    @PostMapping(value = "/upload/{evidenceId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN', 'OFFICER', 'INVESTIGATOR')")
    public ResponseEntity<EvidenceAttachmentResponse> uploadAttachment(
            @PathVariable Long evidenceId,
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal User currentUser) {
        EvidenceAttachmentResponse response = evidenceAttachmentService.uploadAttachment(evidenceId, file, currentUser);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{attachmentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OFFICER', 'INVESTIGATOR')")
    public ResponseEntity<EvidenceAttachmentResponse> getAttachmentMetadata(@PathVariable Long attachmentId) {
        return ResponseEntity.ok(evidenceAttachmentService.getAttachmentById(attachmentId));
    }

    @GetMapping("/evidence/{evidenceId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OFFICER', 'INVESTIGATOR')")
    public ResponseEntity<List<EvidenceAttachmentResponse>> getAttachmentsByEvidence(@PathVariable Long evidenceId) {
        return ResponseEntity.ok(evidenceAttachmentService.getAttachmentsByEvidenceId(evidenceId));
    }

    @GetMapping("/download/{attachmentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OFFICER', 'INVESTIGATOR')")
    public ResponseEntity<Resource> downloadAttachment(@PathVariable Long attachmentId) {
        EvidenceAttachmentResponse metadata = evidenceAttachmentService.getAttachmentById(attachmentId);
        Resource resource = evidenceAttachmentService.downloadAttachment(attachmentId);

        String contentType = metadata.getMimeType();
        if (contentType == null || contentType.isBlank()) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + metadata.getOriginalFileName() + "\"")
                .body(resource);
    }

    @DeleteMapping("/{attachmentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OFFICER', 'INVESTIGATOR')")
    public ResponseEntity<Void> deleteAttachment(
            @PathVariable Long attachmentId,
            @AuthenticationPrincipal User currentUser) {
        evidenceAttachmentService.deleteAttachment(attachmentId, currentUser);
        return ResponseEntity.noContent().build();
    }
}
