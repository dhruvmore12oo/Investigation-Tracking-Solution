package com.example.Investigation_Tracking_Solution.repository;

import com.example.Investigation_Tracking_Solution.model.Criminal;
import com.example.Investigation_Tracking_Solution.model.CriminalStatus;
import com.example.Investigation_Tracking_Solution.model.RiskLevel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CriminalRepo extends JpaRepository<Criminal, Long> {
    List<Criminal> findByFirstNameContainingIgnoreCase(String firstName);

    List<Criminal> findByLastNameContainingIgnoreCase(String lastName);

    List<Criminal> findByStatus(CriminalStatus status);

    List<Criminal> findByRiskLevel(RiskLevel riskLevel);
}
