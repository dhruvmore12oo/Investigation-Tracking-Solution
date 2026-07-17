package com.example.Investigation_Tracking_Solution.repository;

import com.example.Investigation_Tracking_Solution.model.Case;
import com.example.Investigation_Tracking_Solution.model.CasePriority;
import com.example.Investigation_Tracking_Solution.model.CaseStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CaseRepo extends JpaRepository<Case, Long> {

    Optional<Case> findByCaseNumber(String caseNumber);

    List<Case> findByStatus(CaseStatus status);

    List<Case> findByPriority(CasePriority priority);

    List<Case> findByAssignedOfficer_Id(Long officerId);

    List<Case> findByAssignedInvestigator_Id(Long investigatorId);
}