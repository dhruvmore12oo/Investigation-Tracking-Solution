package com.example.Investigation_Tracking_Solution.repository;

import com.example.Investigation_Tracking_Solution.model.Evidence;
import com.example.Investigation_Tracking_Solution.model.EvidenceStatus;
import com.example.Investigation_Tracking_Solution.model.EvidenceType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EvidenceRepo extends JpaRepository<Evidence, Long> {

    Optional<Evidence> findByEvidenceNumber(String evidenceNumber);

    Page<Evidence> findByStatus(EvidenceStatus status, Pageable pageable);

    Page<Evidence> findByEvidenceType(EvidenceType evidenceType, Pageable pageable);

    Page<Evidence> findByInvestigation_Id(Long investigationId, Pageable pageable);

    Page<Evidence> findByCollectedBy_Id(Long officerId, Pageable pageable);

    @Query("SELECT e FROM Evidence e LEFT JOIN e.investigation i WHERE " +
           "LOWER(e.evidenceNumber) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(e.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(e.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(e.storageLocation) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(e.remarks) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(i.investigationNumber) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Evidence> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
