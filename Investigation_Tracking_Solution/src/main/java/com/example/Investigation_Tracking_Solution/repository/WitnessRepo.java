package com.example.Investigation_Tracking_Solution.repository;

import com.example.Investigation_Tracking_Solution.model.ReliabilityLevel;
import com.example.Investigation_Tracking_Solution.model.Witness;
import com.example.Investigation_Tracking_Solution.model.WitnessStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface WitnessRepo extends JpaRepository<Witness, Long> {

    Optional<Witness> findByWitnessNumber(String witnessNumber);

    Optional<Witness> findByPhoneNumber(String phoneNumber);

    Page<Witness> findByWitnessCase_Id(Long caseId, Pageable pageable);

    Page<Witness> findByWitnessStatus(WitnessStatus witnessStatus, Pageable pageable);

    Page<Witness> findByReliabilityLevel(ReliabilityLevel reliabilityLevel, Pageable pageable);

    @Query("SELECT w FROM Witness w LEFT JOIN w.witnessCase c WHERE " +
           "LOWER(w.witnessNumber) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(w.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(w.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(w.phoneNumber) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(w.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(w.city) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(w.state) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(w.statement) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(c.caseNumber) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Witness> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
