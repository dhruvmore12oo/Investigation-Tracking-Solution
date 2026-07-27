package com.example.Investigation_Tracking_Solution.service.impl;

import com.example.Investigation_Tracking_Solution.dto.investigationnote.InvestigationNoteRequest;
import com.example.Investigation_Tracking_Solution.dto.investigationnote.InvestigationNoteResponse;
import com.example.Investigation_Tracking_Solution.exception.BadRequestException;
import com.example.Investigation_Tracking_Solution.exception.ResourceNotFoundException;
import com.example.Investigation_Tracking_Solution.mapper.InvestigationNoteMapper;
import com.example.Investigation_Tracking_Solution.model.*;
import com.example.Investigation_Tracking_Solution.repository.InvestigationNoteRepo;
import com.example.Investigation_Tracking_Solution.repository.InvestigationRepo;
import com.example.Investigation_Tracking_Solution.service.InvestigationNoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InvestigationNoteServiceImpl implements InvestigationNoteService {

    private final InvestigationNoteRepo investigationNoteRepository;
    private final InvestigationRepo investigationRepository;

    @Override
    public InvestigationNoteResponse createNote(InvestigationNoteRequest request) {
        User currentUser = getCurrentUser();

        if (currentUser.getRole() == Role.OFFICER) {
            throw new AccessDeniedException("Officers are not permitted to create investigation notes.");
        }

        Investigation investigation = investigationRepository.findById(request.getInvestigationId())
                .orElseThrow(() -> new ResourceNotFoundException("Could Not Find Investigation With Id : " + request.getInvestigationId()));

        if (investigation.getStatus() == InvestigationStatus.CLOSED) {
            throw new BadRequestException("Cannot add notes to a closed investigation.");
        }

        InvestigationNote note = InvestigationNote.builder()
                .title(request.getTitle())
                .note(request.getNote())
                .noteType(request.getNoteType())
                .investigation(investigation)
                .createdBy(currentUser)
                .build();

        InvestigationNote savedNote = investigationNoteRepository.save(note);
        return InvestigationNoteMapper.toResponse(savedNote);
    }

    @Override
    public InvestigationNoteResponse getNoteById(Long id) {
        InvestigationNote note = findNoteById(id);
        return InvestigationNoteMapper.toResponse(note);
    }

    @Override
    public Page<InvestigationNoteResponse> getAllNotes(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return investigationNoteRepository.findAll(pageable).map(InvestigationNoteMapper::toResponse);
    }

    @Override
    public InvestigationNoteResponse updateNote(Long id, InvestigationNoteRequest request) {
        InvestigationNote note = findNoteById(id);
        User currentUser = getCurrentUser();

        if (currentUser.getRole() != Role.ADMIN && !note.getCreatedBy().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Only the author of the note or ADMIN can edit this note.");
        }

        if (!request.getInvestigationId().equals(note.getInvestigation().getId())) {
            throw new BadRequestException("Investigation note cannot be reassigned to a different investigation.");
        }

        if (note.getInvestigation().getStatus() == InvestigationStatus.CLOSED && currentUser.getRole() != Role.ADMIN) {
            throw new BadRequestException("Notes on closed investigations cannot be edited except by ADMIN.");
        }

        note.setTitle(request.getTitle());
        note.setNote(request.getNote());
        note.setNoteType(request.getNoteType());

        InvestigationNote updatedNote = investigationNoteRepository.save(note);
        return InvestigationNoteMapper.toResponse(updatedNote);
    }

    @Override
    public void deleteNote(Long id) {
        InvestigationNote note = findNoteById(id);
        User currentUser = getCurrentUser();

        if (currentUser.getRole() != Role.ADMIN) {
            throw new AccessDeniedException("Only ADMIN can delete investigation notes.");
        }

        investigationNoteRepository.delete(note);
    }

    @Override
    public Page<InvestigationNoteResponse> getNotesByInvestigation(Long investigationId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return investigationNoteRepository.findByInvestigation_IdOrderByCreatedAtAsc(investigationId, pageable)
                .map(InvestigationNoteMapper::toResponse);
    }

    @Override
    public Page<InvestigationNoteResponse> getNotesByAuthor(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return investigationNoteRepository.findByCreatedBy_Id(userId, pageable)
                .map(InvestigationNoteMapper::toResponse);
    }

    @Override
    public Page<InvestigationNoteResponse> filterByNoteType(NoteType noteType, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return investigationNoteRepository.findByNoteType(noteType, pageable)
                .map(InvestigationNoteMapper::toResponse);
    }

    @Override
    public Page<InvestigationNoteResponse> searchNotes(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return investigationNoteRepository.searchByKeyword(keyword, pageable)
                .map(InvestigationNoteMapper::toResponse);
    }

    private InvestigationNote findNoteById(Long id) {
        return investigationNoteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Could Not Find Investigation Note With Id : " + id));
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof User)) {
            throw new AccessDeniedException("Authenticated user is required for this operation.");
        }
        return (User) authentication.getPrincipal();
    }
}
