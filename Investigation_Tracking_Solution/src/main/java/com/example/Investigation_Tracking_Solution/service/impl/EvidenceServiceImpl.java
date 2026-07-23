package com.example.Investigation_Tracking_Solution.service.impl;

import com.example.Investigation_Tracking_Solution.dto.evidence.EvidenceRequest;
import com.example.Investigation_Tracking_Solution.dto.evidence.EvidenceResponse;
import com.example.Investigation_Tracking_Solution.exception.BadRequestException;
import com.example.Investigation_Tracking_Solution.exception.ResourceNotFoundException;
import com.example.Investigation_Tracking_Solution.mapper.EvidenceMapper;
import com.example.Investigation_Tracking_Solution.model.*;
import com.example.Investigation_Tracking_Solution.repository.EvidenceRepo;
import com.example.Investigation_Tracking_Solution.repository.InvestigationRepo;
import com.example.Investigation_Tracking_Solution.repository.OfficerRepo;
import com.example.Investigation_Tracking_Solution.service.EvidenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EvidenceServiceImpl implements EvidenceService {

    private final EvidenceRepo evidenceRepository;
    private final InvestigationRepo investigationRepository;
    private final OfficerRepo officerRepository;

    @Override
    public EvidenceResponse createEvidence(EvidenceRequest request) {
        if (evidenceRepository.findByEvidenceNumber(request.getEvidenceNumber()).isPresent()) {
            throw new BadRequestException("Evidence number already exists.");
        }

        Investigation investigation = investigationRepository.findById(request.getInvestigationId())
                .orElseThrow(() -> new ResourceNotFoundException("Could Not Find Investigation With Id : " + request.getInvestigationId()));

        Officer officer = officerRepository.findById(request.getCollectedByOfficerId())
                .orElseThrow(() -> new ResourceNotFoundException("Could Not Find Officer With Id : " + request.getCollectedByOfficerId()));

        validateDates(request.getCollectedDate(), request.getReceivedDate());

        Evidence evidence = Evidence.builder()
                .evidenceNumber(request.getEvidenceNumber())
                .title(request.getTitle())
                .description(request.getDescription())
                .evidenceType(request.getEvidenceType())
                .status(request.getStatus())
                .storageLocation(request.getStorageLocation())
                .collectedDate(request.getCollectedDate())
                .receivedDate(request.getReceivedDate())
                .remarks(request.getRemarks())
                .investigation(investigation)
                .collectedBy(officer)
                .build();

        Evidence savedEvidence = evidenceRepository.save(evidence);
        return EvidenceMapper.toResponse(savedEvidence);
    }

    @Override
    public EvidenceResponse getEvidenceById(Long id) {
        Evidence evidence = findEvidenceById(id);
        return EvidenceMapper.toResponse(evidence);
    }

    @Override
    public EvidenceResponse getByEvidenceNumber(String evidenceNumber) {
        Evidence evidence = evidenceRepository.findByEvidenceNumber(evidenceNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Could Not Find Evidence With Number : " + evidenceNumber));
        return EvidenceMapper.toResponse(evidence);
    }

    @Override
    public Page<EvidenceResponse> getAllEvidence(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return evidenceRepository.findAll(pageable).map(EvidenceMapper::toResponse);
    }

    @Override
    public EvidenceResponse updateEvidence(Long id, EvidenceRequest request) {
        Evidence evidence = findEvidenceById(id);
        validateUpdateAuthorization(evidence);

        if (!evidence.getEvidenceNumber().equals(request.getEvidenceNumber()) &&
                evidenceRepository.findByEvidenceNumber(request.getEvidenceNumber()).isPresent()) {
            throw new BadRequestException("Evidence number already exists.");
        }

        User currentUser = getCurrentUser();
        if (currentUser.getRole() == Role.INVESTIGATOR) {
            if (evidence.getInvestigation() == null ||
                    !request.getInvestigationId().equals(evidence.getInvestigation().getId())) {
                throw new AccessDeniedException("Investigators cannot reassign evidence to a different investigation.");
            }
            if (evidence.getCollectedBy() == null ||
                    !request.getCollectedByOfficerId().equals(evidence.getCollectedBy().getId())) {
                throw new AccessDeniedException("Investigators cannot reassign evidence to a different collecting officer.");
            }
        }

        if (!request.getInvestigationId().equals(evidence.getInvestigation().getId())) {
            Investigation newInvestigation = investigationRepository.findById(request.getInvestigationId())
                    .orElseThrow(() -> new ResourceNotFoundException("Could Not Find Investigation With Id : " + request.getInvestigationId()));
            evidence.setInvestigation(newInvestigation);
        }

        if (!request.getCollectedByOfficerId().equals(evidence.getCollectedBy().getId())) {
            Officer newOfficer = officerRepository.findById(request.getCollectedByOfficerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Could Not Find Officer With Id : " + request.getCollectedByOfficerId()));
            evidence.setCollectedBy(newOfficer);
        }

        validateDates(request.getCollectedDate(), request.getReceivedDate());

        evidence.setEvidenceNumber(request.getEvidenceNumber());
        evidence.setTitle(request.getTitle());
        evidence.setDescription(request.getDescription());
        evidence.setEvidenceType(request.getEvidenceType());
        evidence.setStatus(request.getStatus());
        evidence.setStorageLocation(request.getStorageLocation());
        evidence.setCollectedDate(request.getCollectedDate());
        evidence.setReceivedDate(request.getReceivedDate());
        evidence.setRemarks(request.getRemarks());

        Evidence updatedEvidence = evidenceRepository.save(evidence);
        return EvidenceMapper.toResponse(updatedEvidence);
    }

    @Override
    public void deleteEvidence(Long id) {
        Evidence evidence = findEvidenceById(id);

        if (evidence.getStatus() == EvidenceStatus.SUBMITTED_TO_COURT) {
            throw new BadRequestException("Evidence submitted to court cannot be deleted.");
        }

        evidenceRepository.delete(evidence);
    }

    @Override
    public Page<EvidenceResponse> getByInvestigation(Long investigationId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return evidenceRepository.findByInvestigation_Id(investigationId, pageable).map(EvidenceMapper::toResponse);
    }

    @Override
    public Page<EvidenceResponse> getByType(EvidenceType type, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return evidenceRepository.findByEvidenceType(type, pageable).map(EvidenceMapper::toResponse);
    }

    @Override
    public Page<EvidenceResponse> getByStatus(EvidenceStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return evidenceRepository.findByStatus(status, pageable).map(EvidenceMapper::toResponse);
    }

    @Override
    public Page<EvidenceResponse> searchByKeyword(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return evidenceRepository.searchByKeyword(keyword, pageable).map(EvidenceMapper::toResponse);
    }

    private Evidence findEvidenceById(Long id) {
        return evidenceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Could Not Find Evidence With Id : " + id));
    }

    private void validateDates(LocalDateTime collectedDate, LocalDateTime receivedDate) {
        if (collectedDate.isAfter(LocalDateTime.now())) {
            throw new BadRequestException("Collected date cannot be in the future.");
        }

        if (receivedDate != null && receivedDate.isBefore(collectedDate)) {
            throw new BadRequestException("Received date cannot be before collected date.");
        }
    }

    private void validateUpdateAuthorization(Evidence evidence) {
        User currentUser = getCurrentUser();

        if (evidence.getStatus() == EvidenceStatus.VERIFIED && currentUser.getRole() != Role.ADMIN) {
            throw new AccessDeniedException("Verified evidence can only be edited by ADMIN.");
        }

        if (currentUser.getRole() == Role.INVESTIGATOR) {
            if (evidence.getInvestigation() == null ||
                    evidence.getInvestigation().getAssignedInvestigator() == null ||
                    !evidence.getInvestigation().getAssignedInvestigator().getId().equals(currentUser.getId())) {
                throw new AccessDeniedException("You can only update evidence belonging to investigations assigned to you.");
            }
        }
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof User)) {
            throw new AccessDeniedException("Authenticated user is required for this operation.");
        }
        return (User) authentication.getPrincipal();
    }
}
