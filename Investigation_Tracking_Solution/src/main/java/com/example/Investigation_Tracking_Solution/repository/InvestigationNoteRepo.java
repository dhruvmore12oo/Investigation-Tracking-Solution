package com.example.Investigation_Tracking_Solution.repository;

import com.example.Investigation_Tracking_Solution.model.InvestigationNote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvestigationNoteRepo extends JpaRepository<InvestigationNote, Long> {
}
