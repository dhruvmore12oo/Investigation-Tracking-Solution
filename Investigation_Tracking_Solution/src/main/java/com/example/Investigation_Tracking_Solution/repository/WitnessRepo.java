package com.example.Investigation_Tracking_Solution.repository;

import com.example.Investigation_Tracking_Solution.model.Witness;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WitnessRepo extends JpaRepository<Witness, Long> {
}
