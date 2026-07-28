package com.example.Investigation_Tracking_Solution.repository;

import com.example.Investigation_Tracking_Solution.model.Evidence;
import com.example.Investigation_Tracking_Solution.model.EvidenceStatus;
import com.example.Investigation_Tracking_Solution.model.EvidenceType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EvidenceRepo extends JpaRepository<Evidence, Long> {

    Optional<Evidence> findByEvidenceNumber(String evidenceNumber);

    Page<Evidence> findByStatus(EvidenceStatus status, Pageable pageable);

    Page<Evidence> findByEvidenceType(EvidenceType evidenceType, Pageable pageable);

    Page<Evidence> findByInvestigation_Id(Long investigationId, Pageable pageable);

    Page<Evidence> findByCollectedBy_Id(Long officerId, Pageable pageable);

    long countByCollectedBy_Id(Long officerId);

    @Query("SELECT COUNT(e) FROM Evidence e WHERE e.investigation.assignedInvestigator.id = :investigatorId")
    long countByAssignedInvestigator_Id(@Param("investigatorId") Long investigatorId);

    @Query("SELECT e.status as status, COUNT(e) as count FROM Evidence e GROUP BY e.status")
    List<Object[]> findEvidenceCountsByStatus();

    @Query("SELECT e.evidenceType as type, COUNT(e) as count FROM Evidence e GROUP BY e.evidenceType")
    List<Object[]> findEvidenceCountsByType();

    @Query("SELECT e FROM Evidence e LEFT JOIN e.investigation i WHERE " +
           "LOWER(e.evidenceNumber) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(e.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(e.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(e.storageLocation) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(e.remarks) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(i.investigationNumber) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Evidence> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
