package com.example.Investigation_Tracking_Solution.repository;

import com.example.Investigation_Tracking_Solution.model.Criminal;
import com.example.Investigation_Tracking_Solution.model.CriminalStatus;
import com.example.Investigation_Tracking_Solution.model.RiskLevel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CriminalRepo extends JpaRepository<Criminal, Long> {
    List<Criminal> findByFirstNameContainingIgnoreCase(String firstName);

    List<Criminal> findByLastNameContainingIgnoreCase(String lastName);

    Page<Criminal> findByCriminalStatus(CriminalStatus criminalStatus, Pageable pageable);

    Page<Criminal> findByRiskLevel(RiskLevel riskLevel, Pageable pageable);

    Optional<Criminal> findByAadhaarNumber(String aadhaarNumber);

    Optional<Criminal> findByPhoneNumber(String phoneNumber);

    List<Criminal> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName);
}
