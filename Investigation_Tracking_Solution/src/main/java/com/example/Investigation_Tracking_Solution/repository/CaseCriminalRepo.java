package com.example.Investigation_Tracking_Solution.repository;

import com.example.Investigation_Tracking_Solution.model.CaseCriminal;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CaseCriminalRepo extends JpaRepository<CaseCriminal, Long> {
    
    @Query("SELECT cc FROM CaseCriminal cc WHERE cc.caseId.id = :caseId")
    List<CaseCriminal> findByCaseId(@Param("caseId") Long caseId);
}
