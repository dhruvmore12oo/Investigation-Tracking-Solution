package com.example.Investigation_Tracking_Solution.repository;

import com.example.Investigation_Tracking_Solution.model.CasePriority;
import com.example.Investigation_Tracking_Solution.model.Investigation;
import com.example.Investigation_Tracking_Solution.model.InvestigationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

public interface InvestigationRepo extends JpaRepository<Investigation, Long> {

    Optional<Investigation> findByInvestigationNumber(String investigationNumber);

    Optional<Investigation> findByInvestigationCase_Id(Long caseId);

    Page<Investigation> findByStatus(InvestigationStatus status, Pageable pageable);

    Page<Investigation> findByPriority(CasePriority priority, Pageable pageable);

    Page<Investigation> findByAssignedInvestigator_Id(Long investigatorId, Pageable pageable);

    Page<Investigation> findByInvestigationCase_Id(Long caseId, Pageable pageable);

    Page<Investigation> findByStartedAtBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    boolean existsByInvestigationCase_IdAndStatusIn(Long caseId, Collection<InvestigationStatus> statuses);

    @Query("SELECT i FROM Investigation i LEFT JOIN i.investigationCase c WHERE " +
           "LOWER(i.investigationNumber) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(i.summary) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(i.remarks) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(c.title) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Investigation> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
}