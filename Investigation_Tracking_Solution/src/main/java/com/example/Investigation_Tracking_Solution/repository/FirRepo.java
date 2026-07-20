package com.example.Investigation_Tracking_Solution.repository;

import com.example.Investigation_Tracking_Solution.model.Fir;
import com.example.Investigation_Tracking_Solution.model.FirStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface FirRepo extends JpaRepository<Fir, Long> {
    Optional<Fir> findByFirNumber(String firNumber);

    Page<Fir> findByStatus(FirStatus status, Pageable pageable);

    Page<Fir> findByCrimeType(String crimeType, Pageable pageable);

    Page<Fir> findByCity(String city, Pageable pageable);

    Page<Fir> findByOfficerId(Long officerId, Pageable pageable);

    Page<Fir> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    @Query("SELECT f FROM Fir f WHERE LOWER(f.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(f.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(f.crimeType) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Fir> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
}