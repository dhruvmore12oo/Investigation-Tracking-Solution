package com.example.Investigation_Tracking_Solution.repository;

import com.example.Investigation_Tracking_Solution.model.InvestigationNote;
import com.example.Investigation_Tracking_Solution.model.NoteType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface InvestigationNoteRepo extends JpaRepository<InvestigationNote, Long> {

    Page<InvestigationNote> findByInvestigation_Id(Long investigationId, Pageable pageable);

    Page<InvestigationNote> findByInvestigation_IdOrderByCreatedAtAsc(Long investigationId, Pageable pageable);

    Page<InvestigationNote> findByCreatedBy_Id(Long userId, Pageable pageable);

    Page<InvestigationNote> findByNoteType(NoteType noteType, Pageable pageable);

    @Query("SELECT n FROM InvestigationNote n LEFT JOIN n.investigation i LEFT JOIN n.createdBy u WHERE " +
           "LOWER(n.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(n.note) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(i.investigationNumber) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(u.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(u.lastName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<InvestigationNote> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
