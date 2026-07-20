package com.example.Investigation_Tracking_Solution.repository;

import com.example.Investigation_Tracking_Solution.model.Case;
import com.example.Investigation_Tracking_Solution.model.CasePriority;
import com.example.Investigation_Tracking_Solution.model.CaseStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CaseRepo extends JpaRepository<Case, Long> {

    Optional<Case> findByCaseNumber(String caseNumber);

    Page<Case> findByStatus(CaseStatus status, Pageable pageable);

    Page<Case> findByPriority(CasePriority priority, Pageable pageable);

    Page<Case> findByAssignedOfficer_Id(Long officerId, Pageable pageable);

    Page<Case> findByAssignedInvestigator_Id(Long investigatorId, Pageable pageable);

    Page<Case> findByFir_Id(Long firId, Pageable pageable);

    @Query("SELECT c FROM Case c WHERE LOWER(c.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(c.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Case> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
}