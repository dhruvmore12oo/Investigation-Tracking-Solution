package com.example.Investigation_Tracking_Solution.repository;

import com.example.Investigation_Tracking_Solution.model.EvidenceAttachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EvidenceAttachmentRepo extends JpaRepository<EvidenceAttachment, Long> {
    List<EvidenceAttachment> findByEvidence_Id(Long evidenceId);
    Optional<EvidenceAttachment> findByStoredFileName(String storedFileName);
}
