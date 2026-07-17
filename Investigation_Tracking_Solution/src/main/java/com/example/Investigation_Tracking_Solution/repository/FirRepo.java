package com.example.Investigation_Tracking_Solution.repository;

import com.example.Investigation_Tracking_Solution.model.Fir;
import com.example.Investigation_Tracking_Solution.model.FirStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FirRepo extends JpaRepository<Fir, Long> {
    Optional<Fir> findByFirNumber(String firNumber);

    List<Fir> findByStatus(FirStatus status);

    List<Fir> findByCrimeType(String crimeType);

    List<Fir> findByCity(String city);

    List<Fir> findByOfficerId(Long officerId);
}