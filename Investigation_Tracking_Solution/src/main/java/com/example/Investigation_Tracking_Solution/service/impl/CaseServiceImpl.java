package com.example.Investigation_Tracking_Solution.service.impl;

import com.example.Investigation_Tracking_Solution.dto.cases.CaseRequest;
import com.example.Investigation_Tracking_Solution.dto.cases.CaseResponse;
import com.example.Investigation_Tracking_Solution.exception.BadRequestException;
import com.example.Investigation_Tracking_Solution.exception.ResourceNotFoundException;
import com.example.Investigation_Tracking_Solution.mapper.CaseMapper;
import com.example.Investigation_Tracking_Solution.model.*;
import com.example.Investigation_Tracking_Solution.repository.*;
import com.example.Investigation_Tracking_Solution.service.CaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CaseServiceImpl implements CaseService {

    private final CaseRepo caseRepository;
    private final FirRepo firRepository;
    private final OfficerRepo officerRepository;
    private final UserRepo userRepository;
    private final CriminalRepo criminalRepository;
    private final CaseCriminalRepo caseCriminalRepository;

    @Override
    public CaseResponse createCase(CaseRequest request) {
        if (caseRepository.findByCaseNumber(request.getCaseNumber()).isPresent()) {
            throw new BadRequestException("Case number already exists.");
        }

        Fir fir = firRepository.findById(request.getFirId())
                .orElseThrow(() -> new ResourceNotFoundException("Could Not Find FIR With Id : " + request.getFirId()));

        Officer assignedOfficer = null;
        if (request.getAssignedOfficerId() != null) {
            assignedOfficer = officerRepository.findById(request.getAssignedOfficerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Could Not Find Officer With Id : " + request.getAssignedOfficerId()));
        }

        User assignedInvestigator = null;
        if (request.getAssignedInvestigatorId() != null) {
            assignedInvestigator = userRepository.findById(request.getAssignedInvestigatorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Could Not Find Investigator With Id : " + request.getAssignedInvestigatorId()));
        }

        Case caseEntity = Case.builder()
                .caseNumber(request.getCaseNumber())
                .title(request.getTitle())
                .description(request.getDescription())
                .priority(request.getPriority())
                .status(request.getStatus())
                .fir(fir)
                .assignedOfficer(assignedOfficer)
                .assignedInvestigator(assignedInvestigator)
                .build();

        Case savedCase = caseRepository.save(caseEntity);

        if (request.getCriminalIds() != null && !request.getCriminalIds().isEmpty()) {
            for (Long criminalId : request.getCriminalIds()) {
                Criminal criminal = criminalRepository.findById(criminalId)
                        .orElseThrow(() -> new ResourceNotFoundException("Could Not Find Criminal With Id : " + criminalId));
                CaseCriminal caseCriminal = CaseCriminal.builder()
                        .caseId(savedCase)
                        .criminalId(criminal)
                        .roleInCase("SUSPECT")
                        .arrestStatus(ArrestStatus.NOT_ARRESTED)
                        .build();
                caseCriminalRepository.save(caseCriminal);
            }
        }

        return CaseMapper.toResponse(savedCase, request.getCriminalIds());
    }

    @Override
    public CaseResponse getCaseById(Long id) {
        Case caseEntity = caseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Could Not Find Case With Id : " + id));
        return CaseMapper.toResponse(caseEntity, getCriminalIdsForCase(id));
    }

    @Override
    public Page<CaseResponse> getAllCases(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return caseRepository.findAll(pageable)
                .map(caseEntity -> CaseMapper.toResponse(caseEntity, getCriminalIdsForCase(caseEntity.getId())));
    }

    @Override
    public CaseResponse updateCase(Long id, CaseRequest request) {
        Case caseEntity = caseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Could Not Find Case With Id : " + id));

        if (!caseEntity.getCaseNumber().equals(request.getCaseNumber()) &&
                caseRepository.findByCaseNumber(request.getCaseNumber()).isPresent()) {
            throw new BadRequestException("Case number already exists.");
        }

        Fir fir = firRepository.findById(request.getFirId())
                .orElseThrow(() -> new ResourceNotFoundException("Could Not Find FIR With Id : " + request.getFirId()));

        Officer assignedOfficer = null;
        if (request.getAssignedOfficerId() != null) {
            assignedOfficer = officerRepository.findById(request.getAssignedOfficerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Could Not Find Officer With Id : " + request.getAssignedOfficerId()));
        }

        User assignedInvestigator = null;
        if (request.getAssignedInvestigatorId() != null) {
            assignedInvestigator = userRepository.findById(request.getAssignedInvestigatorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Could Not Find Investigator With Id : " + request.getAssignedInvestigatorId()));
        }

        caseEntity.setCaseNumber(request.getCaseNumber());
        caseEntity.setTitle(request.getTitle());
        caseEntity.setDescription(request.getDescription());
        caseEntity.setPriority(request.getPriority());
        caseEntity.setStatus(request.getStatus());
        caseEntity.setFir(fir);
        caseEntity.setAssignedOfficer(assignedOfficer);
        caseEntity.setAssignedInvestigator(assignedInvestigator);

        Case updatedCase = caseRepository.save(caseEntity);

        // Optional: updating CaseCriminals could be complex (add/remove). 
        // For simplicity as requested, we rebuild them if provided.
        if (request.getCriminalIds() != null) {
            List<CaseCriminal> existing = caseCriminalRepository.findByCaseId(id);
            caseCriminalRepository.deleteAll(existing);
            for (Long criminalId : request.getCriminalIds()) {
                Criminal criminal = criminalRepository.findById(criminalId)
                        .orElseThrow(() -> new ResourceNotFoundException("Could Not Find Criminal With Id : " + criminalId));
                CaseCriminal caseCriminal = CaseCriminal.builder()
                        .caseId(updatedCase)
                        .criminalId(criminal)
                        .roleInCase("SUSPECT")
                        .arrestStatus(ArrestStatus.NOT_ARRESTED)
                        .build();
                caseCriminalRepository.save(caseCriminal);
            }
        }

        return CaseMapper.toResponse(updatedCase, request.getCriminalIds() != null ? request.getCriminalIds() : getCriminalIdsForCase(id));
    }

    @Override
    public void deleteCase(Long id) {
        Case caseEntity = caseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Could Not Find Case With Id : " + id));
        List<CaseCriminal> caseCriminals = caseCriminalRepository.findByCaseId(id);
        caseCriminalRepository.deleteAll(caseCriminals);
        caseRepository.delete(caseEntity);
    }

    @Override
    public CaseResponse getByCaseNumber(String caseNumber) {
        Case caseEntity = caseRepository.findByCaseNumber(caseNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Could Not Find Case With Number : " + caseNumber));
        return CaseMapper.toResponse(caseEntity, getCriminalIdsForCase(caseEntity.getId()));
    }

    @Override
    public Page<CaseResponse> getByStatus(CaseStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return caseRepository.findByStatus(status, pageable)
                .map(caseEntity -> CaseMapper.toResponse(caseEntity, getCriminalIdsForCase(caseEntity.getId())));
    }

    @Override
    public Page<CaseResponse> getByPriority(CasePriority priority, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return caseRepository.findByPriority(priority, pageable)
                .map(caseEntity -> CaseMapper.toResponse(caseEntity, getCriminalIdsForCase(caseEntity.getId())));
    }

    @Override
    public Page<CaseResponse> getByAssignedOfficer(Long officerId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return caseRepository.findByAssignedOfficer_Id(officerId, pageable)
                .map(caseEntity -> CaseMapper.toResponse(caseEntity, getCriminalIdsForCase(caseEntity.getId())));
    }

    @Override
    public Page<CaseResponse> getByFir(Long firId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return caseRepository.findByFir_Id(firId, pageable)
                .map(caseEntity -> CaseMapper.toResponse(caseEntity, getCriminalIdsForCase(caseEntity.getId())));
    }

    @Override
    public Page<CaseResponse> searchByKeyword(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return caseRepository.searchByKeyword(keyword, pageable)
                .map(caseEntity -> CaseMapper.toResponse(caseEntity, getCriminalIdsForCase(caseEntity.getId())));
    }

    private List<Long> getCriminalIdsForCase(Long caseId) {
        return caseCriminalRepository.findByCaseId(caseId).stream()
                .map(cc -> cc.getCriminalId().getId())
                .collect(Collectors.toList());
    }
}
