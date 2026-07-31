package com.example.Investigation_Tracking_Solution.service.impl;

import com.example.Investigation_Tracking_Solution.annotation.Auditable;
import com.example.Investigation_Tracking_Solution.dto.investigationnote.InvestigationNoteRequest;
import com.example.Investigation_Tracking_Solution.dto.investigationnote.InvestigationNoteResponse;
import com.example.Investigation_Tracking_Solution.event.InvestigationNoteCreatedEvent;
import com.example.Investigation_Tracking_Solution.exception.BadRequestException;
import com.example.Investigation_Tracking_Solution.exception.ResourceNotFoundException;
import com.example.Investigation_Tracking_Solution.mapper.InvestigationNoteMapper;
import com.example.Investigation_Tracking_Solution.model.*;
import com.example.Investigation_Tracking_Solution.repository.InvestigationNoteRepo;
import com.example.Investigation_Tracking_Solution.repository.InvestigationRepo;
import com.example.Investigation_Tracking_Solution.service.InvestigationNoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InvestigationNoteServiceImpl implements InvestigationNoteService {

    private final InvestigationNoteRepo investigationNoteRepository;
    private final InvestigationRepo investigationRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    @Auditable(action = AuditAction.CREATE, module = AuditModule.INVESTIGATION_NOTE, entityType = "INVESTIGATION_NOTE", description = "Authored investigation note")
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

        Long investigatorUserId = (investigation.getAssignedInvestigator() != null) ? investigation.getAssignedInvestigator().getId() : null;

        eventPublisher.publishEvent(InvestigationNoteCreatedEvent.builder()
                .noteId(savedNote.getId())
                .title(savedNote.getTitle())
                .investigationId(investigation.getId())
                .investigationNumber(investigation.getInvestigationNumber())
                .assignedInvestigatorUserId(investigatorUserId)
                .triggeredByUserId(currentUser.getId())
                .build());

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
    @Transactional
    @Auditable(action = AuditAction.UPDATE, module = AuditModule.INVESTIGATION_NOTE, entityType = "INVESTIGATION_NOTE", description = "Updated investigation note")
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
    @Transactional
    @Auditable(action = AuditAction.DELETE, module = AuditModule.INVESTIGATION_NOTE, entityType = "INVESTIGATION_NOTE", description = "Deleted investigation note")
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
