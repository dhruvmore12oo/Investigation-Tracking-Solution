package com.example.Investigation_Tracking_Solution.repository;

import com.example.Investigation_Tracking_Solution.model.Criminal;
import com.example.Investigation_Tracking_Solution.model.CriminalStatus;
import com.example.Investigation_Tracking_Solution.model.RiskLevel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CriminalRepo extends JpaRepository<Criminal, Long> {
    List<Criminal> findByFirstNameContainingIgnoreCase(String firstName);

    List<Criminal> findByLastNameContainingIgnoreCase(String lastName);

    List<Criminal> findByCriminalStatus(CriminalStatus criminalStatus);

    List<Criminal> findByRiskLevel(RiskLevel riskLevel);

    Optional<Criminal> findByAadhaarNumber(String aadhaarNumber);

    Optional<Criminal> findByPhoneNumber(String phoneNumber);

    List<Criminal> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName);
}
