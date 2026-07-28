package com.example.Investigation_Tracking_Solution.repository;

import com.example.Investigation_Tracking_Solution.model.Case;
import com.example.Investigation_Tracking_Solution.model.CasePriority;
import com.example.Investigation_Tracking_Solution.model.CaseStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CaseRepo extends JpaRepository<Case, Long> {

    Optional<Case> findByCaseNumber(String caseNumber);

    Page<Case> findByStatus(CaseStatus status, Pageable pageable);

    Page<Case> findByPriority(CasePriority priority, Pageable pageable);

    Page<Case> findByAssignedOfficer_Id(Long officerId, Pageable pageable);

    Page<Case> findByAssignedInvestigator_Id(Long investigatorId, Pageable pageable);

    Page<Case> findByFir_Id(Long firId, Pageable pageable);

    long countByAssignedOfficer_Id(Long officerId);

    long countByAssignedInvestigator_Id(Long investigatorId);

    long countByStatus(CaseStatus status);

    @Query("SELECT c.status as status, COUNT(c) as count FROM Case c GROUP BY c.status")
    List<Object[]> findCaseCountsByStatus();

    @Query("SELECT YEAR(c.createdAt) as year, MONTH(c.createdAt) as month, COUNT(c) as count FROM Case c GROUP BY YEAR(c.createdAt), MONTH(c.createdAt) ORDER BY year DESC, month DESC")
    List<Object[]> findMonthlyCaseCounts();

    @Query("SELECT c FROM Case c WHERE LOWER(c.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(c.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Case> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
}