package com.example.Investigation_Tracking_Solution.service;

import com.example.Investigation_Tracking_Solution.dto.investigationnote.InvestigationNoteRequest;
import com.example.Investigation_Tracking_Solution.dto.investigationnote.InvestigationNoteResponse;
import com.example.Investigation_Tracking_Solution.model.NoteType;
import org.springframework.data.domain.Page;

public interface InvestigationNoteService {
    InvestigationNoteResponse createNote(InvestigationNoteRequest request);
    InvestigationNoteResponse getNoteById(Long id);
    Page<InvestigationNoteResponse> getAllNotes(int page, int size);
    InvestigationNoteResponse updateNote(Long id, InvestigationNoteRequest request);
    void deleteNote(Long id);
    Page<InvestigationNoteResponse> getNotesByInvestigation(Long investigationId, int page, int size);
    Page<InvestigationNoteResponse> getNotesByAuthor(Long userId, int page, int size);
    Page<InvestigationNoteResponse> filterByNoteType(NoteType noteType, int page, int size);
    Page<InvestigationNoteResponse> searchNotes(String keyword, int page, int size);
}
