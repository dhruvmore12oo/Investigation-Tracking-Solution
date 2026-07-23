package com.example.Investigation_Tracking_Solution.service.impl;

import com.example.Investigation_Tracking_Solution.dto.investigation.InvestigationRequest;
import com.example.Investigation_Tracking_Solution.dto.investigation.InvestigationResponse;
import com.example.Investigation_Tracking_Solution.exception.BadRequestException;
import com.example.Investigation_Tracking_Solution.exception.ResourceNotFoundException;
import com.example.Investigation_Tracking_Solution.mapper.InvestigationMapper;
import com.example.Investigation_Tracking_Solution.model.*;
import com.example.Investigation_Tracking_Solution.repository.CaseRepo;
import com.example.Investigation_Tracking_Solution.repository.InvestigationRepo;
import com.example.Investigation_Tracking_Solution.repository.UserRepo;
import com.example.Investigation_Tracking_Solution.service.InvestigationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class InvestigationServiceImpl implements InvestigationService {

    private static final Set<InvestigationStatus> ACTIVE_STATUSES = EnumSet.of(
            InvestigationStatus.OPEN,
            InvestigationStatus.IN_PROGRESS,
            InvestigationStatus.ON_HOLD
    );

    private final InvestigationRepo investigationRepository;
    private final CaseRepo caseRepository;
    private final UserRepo userRepository;

    @Override
    public InvestigationResponse createInvestigation(InvestigationRequest request) {
        if (investigationRepository.findByInvestigationNumber(request.getInvestigationNumber()).isPresent()) {
            throw new BadRequestException("Investigation number already exists.");
        }

        Case investigationCase = caseRepository.findById(request.getCaseId())
                .orElseThrow(() -> new ResourceNotFoundException("Could Not Find Case With Id : " + request.getCaseId()));

        if (investigationRepository.findByInvestigationCase_Id(request.getCaseId()).isPresent()) {
            throw new BadRequestException("An investigation already exists for this case.");
        }

        if (investigationRepository.existsByInvestigationCase_IdAndStatusIn(request.getCaseId(), ACTIVE_STATUSES)) {
            throw new BadRequestException("An active investigation already exists for this case.");
        }

        User assignedInvestigator = validateAndGetInvestigator(request.getAssignedInvestigatorId());
        validateDates(request.getStartDate(), request.getExpectedCompletionDate(), request.getCompletedDate());
        validateCompletedStatus(request.getStatus(), request.getCompletedDate());

        Investigation investigation = Investigation.builder()
                .investigationNumber(request.getInvestigationNumber())
                .investigationCase(investigationCase)
                .assignedInvestigator(assignedInvestigator)
                .status(request.getStatus())
                .priority(request.getPriority())
                .startedAt(request.getStartDate())
                .expectedCompletionDate(request.getExpectedCompletionDate())
                .completedDate(request.getCompletedDate())
                .summary(request.getSummary())
                .remarks(request.getRemarks())
                .build();

        applyStatusTimestamps(investigation, request.getStatus(), request.getCompletedDate());

        Investigation savedInvestigation = investigationRepository.save(investigation);
        return InvestigationMapper.toResponse(savedInvestigation);
    }

    @Override
    public InvestigationResponse getInvestigationById(Long id) {
        Investigation investigation = findInvestigationById(id);
        return InvestigationMapper.toResponse(investigation);
    }

    @Override
    public InvestigationResponse getByInvestigationNumber(String investigationNumber) {
        Investigation investigation = investigationRepository.findByInvestigationNumber(investigationNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Could Not Find Investigation With Number : " + investigationNumber));
        return InvestigationMapper.toResponse(investigation);
    }

    @Override
    public Page<InvestigationResponse> getAllInvestigations(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return investigationRepository.findAll(pageable).map(InvestigationMapper::toResponse);
    }

    @Override
    public InvestigationResponse updateInvestigation(Long id, InvestigationRequest request) {
        Investigation investigation = findInvestigationById(id);
        validateUpdateAuthorization(investigation);

        if (!investigation.getInvestigationNumber().equals(request.getInvestigationNumber()) &&
                investigationRepository.findByInvestigationNumber(request.getInvestigationNumber()).isPresent()) {
            throw new BadRequestException("Investigation number already exists.");
        }

        User currentUser = getCurrentUser();
        if (currentUser.getRole() == Role.INVESTIGATOR) {
            if (investigation.getInvestigationCase() == null
                    || !request.getCaseId().equals(investigation.getInvestigationCase().getId())) {
                throw new AccessDeniedException("Investigators cannot reassign investigations to a different case.");
            }
            if (investigation.getAssignedInvestigator() == null
                    || !request.getAssignedInvestigatorId().equals(investigation.getAssignedInvestigator().getId())) {
                throw new AccessDeniedException("Investigators cannot reassign investigations to a different investigator.");
            }
        }

        if (!request.getCaseId().equals(investigation.getInvestigationCase().getId())) {
            Case newCase = caseRepository.findById(request.getCaseId())
                    .orElseThrow(() -> new ResourceNotFoundException("Could Not Find Case With Id : " + request.getCaseId()));

            if (investigationRepository.findByInvestigationCase_Id(request.getCaseId()).isPresent()) {
                throw new BadRequestException("An investigation already exists for this case.");
            }

            investigation.setInvestigationCase(newCase);
        }

        User assignedInvestigator = validateAndGetInvestigator(request.getAssignedInvestigatorId());
        validateDates(request.getStartDate(), request.getExpectedCompletionDate(), request.getCompletedDate());
        validateCompletedStatus(request.getStatus(), request.getCompletedDate());

        investigation.setInvestigationNumber(request.getInvestigationNumber());
        investigation.setAssignedInvestigator(assignedInvestigator);
        investigation.setStatus(request.getStatus());
        investigation.setPriority(request.getPriority());
        investigation.setStartedAt(request.getStartDate());
        investigation.setExpectedCompletionDate(request.getExpectedCompletionDate());
        investigation.setCompletedDate(request.getCompletedDate());
        investigation.setSummary(request.getSummary());
        investigation.setRemarks(request.getRemarks());

        applyStatusTimestamps(investigation, request.getStatus(), request.getCompletedDate());

        Investigation updatedInvestigation = investigationRepository.save(investigation);
        return InvestigationMapper.toResponse(updatedInvestigation);
    }

    @Override
    public void deleteInvestigation(Long id) {
        Investigation investigation = findInvestigationById(id);
        investigationRepository.delete(investigation);
    }

    @Override
    public Page<InvestigationResponse> getByStatus(InvestigationStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return investigationRepository.findByStatus(status, pageable).map(InvestigationMapper::toResponse);
    }

    @Override
    public Page<InvestigationResponse> getByPriority(CasePriority priority, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return investigationRepository.findByPriority(priority, pageable).map(InvestigationMapper::toResponse);
    }

    @Override
    public Page<InvestigationResponse> getByInvestigator(Long investigatorId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return investigationRepository.findByAssignedInvestigator_Id(investigatorId, pageable)
                .map(InvestigationMapper::toResponse);
    }

    @Override
    public Page<InvestigationResponse> getByCase(Long caseId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return investigationRepository.findByInvestigationCase_Id(caseId, pageable)
                .map(InvestigationMapper::toResponse);
    }

    @Override
    public Page<InvestigationResponse> getByDateRange(LocalDateTime startDate, LocalDateTime endDate, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return investigationRepository.findByStartedAtBetween(startDate, endDate, pageable)
                .map(InvestigationMapper::toResponse);
    }

    @Override
    public Page<InvestigationResponse> searchByKeyword(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return investigationRepository.searchByKeyword(keyword, pageable).map(InvestigationMapper::toResponse);
    }

    private Investigation findInvestigationById(Long id) {
        return investigationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Could Not Find Investigation With Id : " + id));
    }

    private User validateAndGetInvestigator(Long investigatorId) {
        User investigator = userRepository.findById(investigatorId)
                .orElseThrow(() -> new ResourceNotFoundException("Could Not Find Investigator With Id : " + investigatorId));

        if (investigator.getRole() != Role.INVESTIGATOR) {
            throw new BadRequestException("Assigned user must have the INVESTIGATOR role.");
        }

        return investigator;
    }

    private void validateDates(LocalDateTime startDate, LocalDateTime expectedCompletionDate, LocalDateTime completedDate) {
        if (expectedCompletionDate != null && expectedCompletionDate.isBefore(startDate)) {
            throw new BadRequestException("Expected completion date cannot be before start date.");
        }

        if (completedDate != null && completedDate.isBefore(startDate)) {
            throw new BadRequestException("Completed date cannot be before start date.");
        }
    }

    private void validateCompletedStatus(InvestigationStatus status, LocalDateTime completedDate) {
        if (status == InvestigationStatus.COMPLETED && completedDate == null) {
            throw new BadRequestException("Completed date is required when investigation status is COMPLETED.");
        }
    }

    private void applyStatusTimestamps(Investigation investigation, InvestigationStatus status, LocalDateTime completedDate) {
        if (status == InvestigationStatus.COMPLETED) {
            investigation.setCompletedDate(completedDate);
        }

        if (status == InvestigationStatus.CLOSED) {
            investigation.setClosedAt(LocalDateTime.now());
        }
    }

    private void validateUpdateAuthorization(Investigation investigation) {
        User currentUser = getCurrentUser();

        if (investigation.getStatus() == InvestigationStatus.CLOSED && currentUser.getRole() != Role.ADMIN) {
            throw new AccessDeniedException("Closed investigations can only be modified by ADMIN.");
        }

        if (currentUser.getRole() == Role.INVESTIGATOR) {
            if (investigation.getAssignedInvestigator() == null ||
                    !investigation.getAssignedInvestigator().getId().equals(currentUser.getId())) {
                throw new AccessDeniedException("You can only update investigations assigned to you.");
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
