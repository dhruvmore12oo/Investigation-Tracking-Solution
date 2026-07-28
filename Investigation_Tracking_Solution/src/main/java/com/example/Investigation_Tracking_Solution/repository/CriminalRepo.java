package com.example.Investigation_Tracking_Solution.repository;

import com.example.Investigation_Tracking_Solution.model.Criminal;
import com.example.Investigation_Tracking_Solution.model.CriminalStatus;
import com.example.Investigation_Tracking_Solution.model.RiskLevel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    @Query("SELECT c.city as city, COUNT(c) as count FROM Criminal c WHERE c.city IS NOT NULL AND TRIM(c.city) <> '' GROUP BY c.city")
    List<Object[]> findCriminalCountsByCity();

    @Query("SELECT c FROM Criminal c WHERE LOWER(c.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(c.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(c.city) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(c.address) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Criminal> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
