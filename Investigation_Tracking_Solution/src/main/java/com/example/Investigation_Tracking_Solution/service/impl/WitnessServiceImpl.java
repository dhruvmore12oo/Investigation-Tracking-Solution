package com.example.Investigation_Tracking_Solution.service.impl;

import com.example.Investigation_Tracking_Solution.dto.witness.WitnessRequest;
import com.example.Investigation_Tracking_Solution.dto.witness.WitnessResponse;
import com.example.Investigation_Tracking_Solution.exception.BadRequestException;
import com.example.Investigation_Tracking_Solution.exception.ResourceNotFoundException;
import com.example.Investigation_Tracking_Solution.mapper.WitnessMapper;
import com.example.Investigation_Tracking_Solution.model.*;
import com.example.Investigation_Tracking_Solution.repository.CaseRepo;
import com.example.Investigation_Tracking_Solution.repository.WitnessRepo;
import com.example.Investigation_Tracking_Solution.service.WitnessService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WitnessServiceImpl implements WitnessService {

    private final WitnessRepo witnessRepository;
    private final CaseRepo caseRepository;

    @Override
    public WitnessResponse createWitness(WitnessRequest request) {
        if (witnessRepository.findByWitnessNumber(request.getWitnessNumber()).isPresent()) {
            throw new BadRequestException("Witness number already exists.");
        }

        if (witnessRepository.findByPhoneNumber(request.getPhoneNumber()).isPresent()) {
            throw new BadRequestException("Phone number already registered for another witness.");
        }

        Case witnessCase = caseRepository.findById(request.getCaseId())
                .orElseThrow(() -> new ResourceNotFoundException("Could Not Find Case With Id : " + request.getCaseId()));

        if (witnessCase.getStatus() == CaseStatus.CLOSED) {
            throw new BadRequestException("Cannot add or assign witness to a closed case.");
        }

        validateDateOfBirth(request.getDateOfBirth());

        Witness witness = Witness.builder()
                .witnessNumber(request.getWitnessNumber())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .gender(request.getGender())
                .dateOfBirth(request.getDateOfBirth())
                .phoneNumber(request.getPhoneNumber())
                .email(request.getEmail())
                .address(request.getAddress())
                .city(request.getCity())
                .state(request.getState())
                .statement(request.getStatement())
                .reliabilityLevel(request.getReliabilityLevel())
                .witnessStatus(request.getWitnessStatus())
                .witnessCase(witnessCase)
                .build();

        Witness savedWitness = witnessRepository.save(witness);
        return WitnessMapper.toResponse(savedWitness);
    }

    @Override
    public WitnessResponse getWitnessById(Long id) {
        Witness witness = findWitnessById(id);
        validateReadAuthorization(witness);
        return WitnessMapper.toResponse(witness);
    }

    @Override
    public WitnessResponse getByWitnessNumber(String witnessNumber) {
        Witness witness = witnessRepository.findByWitnessNumber(witnessNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Could Not Find Witness With Number : " + witnessNumber));
        validateReadAuthorization(witness);
        return WitnessMapper.toResponse(witness);
    }

    @Override
    public Page<WitnessResponse> getAllWitnesses(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Witness> witnessPage = witnessRepository.findAll(pageable);
        return filterAndMapPage(witnessPage, pageable);
    }

    @Override
    public WitnessResponse updateWitness(Long id, WitnessRequest request) {
        Witness witness = findWitnessById(id);
        validateUpdateAuthorization(witness);

        if (!witness.getWitnessNumber().equals(request.getWitnessNumber()) &&
                witnessRepository.findByWitnessNumber(request.getWitnessNumber()).isPresent()) {
            throw new BadRequestException("Witness number already exists.");
        }

        if (!witness.getPhoneNumber().equals(request.getPhoneNumber()) &&
                witnessRepository.findByPhoneNumber(request.getPhoneNumber()).isPresent()) {
            throw new BadRequestException("Phone number already registered for another witness.");
        }

        User currentUser = getCurrentUser();
        if (currentUser.getRole() == Role.INVESTIGATOR) {
            if (witness.getWitnessCase() == null ||
                    !request.getCaseId().equals(witness.getWitnessCase().getId())) {
                throw new AccessDeniedException("Investigators cannot reassign witnesses to a different case.");
            }
        }

        if (!request.getCaseId().equals(witness.getWitnessCase().getId())) {
            Case targetCase = caseRepository.findById(request.getCaseId())
                    .orElseThrow(() -> new ResourceNotFoundException("Could Not Find Case With Id : " + request.getCaseId()));

            if (witness.getWitnessCase().getStatus() == CaseStatus.CLOSED || targetCase.getStatus() == CaseStatus.CLOSED) {
                throw new BadRequestException("Witness cannot be reassigned to or from a closed case.");
            }

            witness.setWitnessCase(targetCase);
        }

        validateDateOfBirth(request.getDateOfBirth());

        witness.setWitnessNumber(request.getWitnessNumber());
        witness.setFirstName(request.getFirstName());
        witness.setLastName(request.getLastName());
        witness.setGender(request.getGender());
        witness.setDateOfBirth(request.getDateOfBirth());
        witness.setPhoneNumber(request.getPhoneNumber());
        witness.setEmail(request.getEmail());
        witness.setAddress(request.getAddress());
        witness.setCity(request.getCity());
        witness.setState(request.getState());
        witness.setStatement(request.getStatement());
        witness.setReliabilityLevel(request.getReliabilityLevel());
        witness.setWitnessStatus(request.getWitnessStatus());

        Witness updatedWitness = witnessRepository.save(witness);
        return WitnessMapper.toResponse(updatedWitness);
    }

    @Override
    public void deleteWitness(Long id) {
        Witness witness = findWitnessById(id);
        witnessRepository.delete(witness);
    }

    @Override
    public Page<WitnessResponse> getByCase(Long caseId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Witness> witnessPage = witnessRepository.findByWitnessCase_Id(caseId, pageable);
        return filterAndMapPage(witnessPage, pageable);
    }

    @Override
    public Page<WitnessResponse> getByStatus(WitnessStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Witness> witnessPage = witnessRepository.findByWitnessStatus(status, pageable);
        return filterAndMapPage(witnessPage, pageable);
    }

    @Override
    public Page<WitnessResponse> getByReliabilityLevel(ReliabilityLevel level, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Witness> witnessPage = witnessRepository.findByReliabilityLevel(level, pageable);
        return filterAndMapPage(witnessPage, pageable);
    }

    @Override
    public Page<WitnessResponse> searchByKeyword(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Witness> witnessPage = witnessRepository.searchByKeyword(keyword, pageable);
        return filterAndMapPage(witnessPage, pageable);
    }

    private Page<WitnessResponse> filterAndMapPage(Page<Witness> witnessPage, Pageable pageable) {
        List<WitnessResponse> filteredContent = witnessPage.getContent().stream()
                .filter(this::canUserAccessWitness)
                .map(WitnessMapper::toResponse)
                .toList();

        return new PageImpl<>(filteredContent, pageable, witnessPage.getTotalElements());
    }

    private Witness findWitnessById(Long id) {
        return witnessRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Could Not Find Witness With Id : " + id));
    }

    private void validateDateOfBirth(LocalDate dateOfBirth) {
        if (dateOfBirth != null && dateOfBirth.isAfter(LocalDate.now())) {
            throw new BadRequestException("Date of birth cannot be in the future.");
        }
    }

    private boolean canUserAccessWitness(Witness witness) {
        User currentUser = getCurrentUser();

        if (witness.getWitnessStatus() == WitnessStatus.PROTECTED) {
            if (currentUser.getRole() == Role.ADMIN) {
                return true;
            }
            return isAssignedInvestigator(witness, currentUser);
        }

        return true;
    }

    private void validateReadAuthorization(Witness witness) {
        if (!canUserAccessWitness(witness)) {
            throw new AccessDeniedException("Protected witnesses can only be accessed or modified by ADMIN or the assigned investigator.");
        }
    }

    private void validateUpdateAuthorization(Witness witness) {
        User currentUser = getCurrentUser();

        if (witness.getWitnessStatus() == WitnessStatus.PROTECTED && currentUser.getRole() != Role.ADMIN) {
            if (!isAssignedInvestigator(witness, currentUser)) {
                throw new AccessDeniedException("Protected witnesses can only be accessed or modified by ADMIN or the assigned investigator.");
            }
        }

        if (currentUser.getRole() == Role.INVESTIGATOR) {
            if (!isAssignedInvestigator(witness, currentUser)) {
                throw new AccessDeniedException("You can only update witnesses belonging to cases assigned to you.");
            }
        }
    }

    private boolean isAssignedInvestigator(Witness witness, User user) {
        if (witness.getWitnessCase() == null) {
            return false;
        }
        User assignedInvestigator = witness.getWitnessCase().getAssignedInvestigator();
        return assignedInvestigator != null && assignedInvestigator.getId().equals(user.getId());
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof User)) {
            throw new AccessDeniedException("Authenticated user is required for this operation.");
        }
        return (User) authentication.getPrincipal();
    }
}
