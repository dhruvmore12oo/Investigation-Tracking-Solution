package com.example.Investigation_Tracking_Solution.repository;

import com.example.Investigation_Tracking_Solution.model.Investigation;
import com.example.Investigation_Tracking_Solution.model.InvestigationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InvestigationRepo extends JpaRepository<Investigation, Long> {

    Optional<Investigation> findByInvestigationCase_Id(Long caseId);

    List<Investigation> findByStatus(InvestigationStatus status);
}