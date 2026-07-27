package com.example.Investigation_Tracking_Solution.controller;

import com.example.Investigation_Tracking_Solution.dto.investigationnote.InvestigationNoteRequest;
import com.example.Investigation_Tracking_Solution.dto.investigationnote.InvestigationNoteResponse;
import com.example.Investigation_Tracking_Solution.model.NoteType;
import com.example.Investigation_Tracking_Solution.service.InvestigationNoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/investigation-notes")
@RequiredArgsConstructor
public class InvestigationNoteController {

    private final InvestigationNoteService investigationNoteService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'INVESTIGATOR')")
    public ResponseEntity<InvestigationNoteResponse> createNote(@Valid @RequestBody InvestigationNoteRequest request) {
        return new ResponseEntity<>(investigationNoteService.createNote(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OFFICER', 'INVESTIGATOR')")
    public ResponseEntity<InvestigationNoteResponse> getNoteById(@PathVariable Long id) {
        return ResponseEntity.ok(investigationNoteService.getNoteById(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OFFICER', 'INVESTIGATOR')")
    public ResponseEntity<Page<InvestigationNoteResponse>> getAllNotes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(investigationNoteService.getAllNotes(page, size));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'INVESTIGATOR')")
    public ResponseEntity<InvestigationNoteResponse> updateNote(
            @PathVariable Long id,
            @Valid @RequestBody InvestigationNoteRequest request) {
        return ResponseEntity.ok(investigationNoteService.updateNote(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteNote(@PathVariable Long id) {
        investigationNoteService.deleteNote(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/investigation/{investigationId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OFFICER', 'INVESTIGATOR')")
    public ResponseEntity<Page<InvestigationNoteResponse>> getNotesByInvestigation(
            @PathVariable Long investigationId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(investigationNoteService.getNotesByInvestigation(investigationId, page, size));
    }

    @GetMapping("/author/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OFFICER', 'INVESTIGATOR')")
    public ResponseEntity<Page<InvestigationNoteResponse>> getNotesByAuthor(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(investigationNoteService.getNotesByAuthor(userId, page, size));
    }

    @GetMapping("/type/{noteType}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OFFICER', 'INVESTIGATOR')")
    public ResponseEntity<Page<InvestigationNoteResponse>> filterByNoteType(
            @PathVariable NoteType noteType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(investigationNoteService.filterByNoteType(noteType, page, size));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'OFFICER', 'INVESTIGATOR')")
    public ResponseEntity<Page<InvestigationNoteResponse>> searchNotes(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(investigationNoteService.searchNotes(keyword, page, size));
    }
}
