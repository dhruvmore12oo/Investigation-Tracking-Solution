package com.example.Investigation_Tracking_Solution.service.impl;

import com.example.Investigation_Tracking_Solution.annotation.Auditable;
import com.example.Investigation_Tracking_Solution.dto.officer.OfficerRequest;
import com.example.Investigation_Tracking_Solution.dto.officer.OfficerResponse;
import com.example.Investigation_Tracking_Solution.exception.BadRequestException;
import com.example.Investigation_Tracking_Solution.exception.ResourceNotFoundException;
import com.example.Investigation_Tracking_Solution.mapper.OfficerMapper;
import com.example.Investigation_Tracking_Solution.model.AuditAction;
import com.example.Investigation_Tracking_Solution.model.AuditModule;
import com.example.Investigation_Tracking_Solution.model.Officer;
import com.example.Investigation_Tracking_Solution.model.User;
import com.example.Investigation_Tracking_Solution.repository.OfficerRepo;
import com.example.Investigation_Tracking_Solution.repository.UserRepo;
import com.example.Investigation_Tracking_Solution.service.OfficerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OfficerServiceImpl implements OfficerService {

    private final OfficerRepo officerRepository;
    private final UserRepo userRepository;

    @Override
    @Auditable(action = AuditAction.CREATE, module = AuditModule.OFFICER, entityType = "OFFICER", description = "Created officer")
    public OfficerResponse createOfficer(OfficerRequest request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Could Not Find Officer With Id : " + request.getUserId()));

        if (officerRepository.existsByBadgeNumber(request.getBadgeNumber())) {
            throw new BadRequestException("Badge number already exists.");
        }

        if (officerRepository.existsByUser(user)) {
            throw new BadRequestException("This user is already assigned as an officer.");
        }

        Officer officer = Officer.builder()
                .badgeNumber(request.getBadgeNumber())
                .rank(request.getRank())
                .department(request.getDepartment())
                .user(user)
                .build();

        Officer savedOfficer = officerRepository.save(officer);

        return OfficerMapper.toResponse(savedOfficer);
    }

    @Override
    public OfficerResponse getOfficerById(Long id) {
        Officer officer = officerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Could Not Find Officer With Id : " + id));

        return OfficerMapper.toResponse(officer);
    }

    @Override
    public Page<OfficerResponse> getAllOfficers(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        return officerRepository.findAll(pageable).map(OfficerMapper::toResponse);
    }

    @Override
    @Auditable(action = AuditAction.UPDATE, module = AuditModule.OFFICER, entityType = "OFFICER", description = "Updated officer")
    public OfficerResponse updateOfficer(Long id, OfficerRequest request) {
        Officer officer = officerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Could Not Find Officer With Id : " + id));

        officer.setBadgeNumber(request.getBadgeNumber());
        officer.setRank(request.getRank());
        officer.setDepartment(request.getDepartment());

        Officer updatedOfficer = officerRepository.save(officer);

        return OfficerMapper.toResponse(updatedOfficer);
    }

    @Override
    @Auditable(action = AuditAction.DELETE, module = AuditModule.OFFICER, entityType = "OFFICER", description = "Deleted officer")
    public void deleteOfficer(Long id){
        Officer officer = officerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Could Not Find Officer With Id : " + id));

        officerRepository.delete(officer);
    }

    @Override
    public OfficerResponse getOfficerByBadgeNumber(String badgeNumber) {
        Officer officer = officerRepository.findByBadgeNumber(badgeNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Could Not Find Officer With BadgeNumber : " + badgeNumber));

        return OfficerMapper.toResponse(officer);
    }
}
