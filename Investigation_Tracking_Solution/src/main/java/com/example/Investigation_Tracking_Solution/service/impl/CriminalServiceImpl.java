package com.example.Investigation_Tracking_Solution.service.impl;

import com.example.Investigation_Tracking_Solution.dto.criminal.CriminalRequest;
import com.example.Investigation_Tracking_Solution.dto.criminal.CriminalResponse;
import com.example.Investigation_Tracking_Solution.exception.BadRequestException;
import com.example.Investigation_Tracking_Solution.exception.ResourceNotFoundException;
import com.example.Investigation_Tracking_Solution.mapper.CriminalMapper;
import com.example.Investigation_Tracking_Solution.model.Criminal;
import com.example.Investigation_Tracking_Solution.repository.CriminalRepo;
import com.example.Investigation_Tracking_Solution.service.CriminalService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CriminalServiceImpl implements CriminalService {

    private final CriminalRepo criminalRepository;

    @Override
    public CriminalResponse createCriminal(CriminalRequest request) {
        if (criminalRepository.findByAadhaarNumber(request.getAadhaarNumber()).isPresent()) {
            throw new BadRequestException("Aadhaar number already exists.");
        }

        if (criminalRepository.findByPhoneNumber(request.getPhoneNumber()).isPresent()) {
            throw new BadRequestException("Phone number already exists.");
        }

        Criminal criminal = Criminal.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .gender(request.getGender())
                .dateOfBirth(request.getDateOfBirth())
                .phoneNumber(request.getPhoneNumber())
                .aadhaarNumber(request.getAadhaarNumber())
                .address(request.getAddress())
                .city(request.getCity())
                .state(request.getState())
                .criminalStatus(request.getCriminalStatus())
                .riskLevel(request.getRiskLevel())
                .build();

        Criminal savedCriminal = criminalRepository.save(criminal);

        return CriminalMapper.toResponse(savedCriminal);
    }

    @Override
    public CriminalResponse getCriminalById(Long id) {
        Criminal criminal = criminalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Could Not Find Criminal With Id : " + id));

        return CriminalMapper.toResponse(criminal);
    }

    @Override
    public Page<CriminalResponse> getAllCriminals(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return criminalRepository.findAll(pageable).map(CriminalMapper::toResponse);
    }

    @Override
    public CriminalResponse updateCriminal(Long id, CriminalRequest request) {
        Criminal criminal = criminalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Could Not Find Criminal With Id : " + id));

        // Check uniqueness if updating fields
        if (!criminal.getAadhaarNumber().equals(request.getAadhaarNumber()) &&
                criminalRepository.findByAadhaarNumber(request.getAadhaarNumber()).isPresent()) {
            throw new BadRequestException("Aadhaar number already exists.");
        }

        if (!criminal.getPhoneNumber().equals(request.getPhoneNumber()) &&
                criminalRepository.findByPhoneNumber(request.getPhoneNumber()).isPresent()) {
            throw new BadRequestException("Phone number already exists.");
        }

        criminal.setFirstName(request.getFirstName());
        criminal.setLastName(request.getLastName());
        criminal.setGender(request.getGender());
        criminal.setDateOfBirth(request.getDateOfBirth());
        criminal.setPhoneNumber(request.getPhoneNumber());
        criminal.setAadhaarNumber(request.getAadhaarNumber());
        criminal.setAddress(request.getAddress());
        criminal.setCity(request.getCity());
        criminal.setState(request.getState());
        criminal.setCriminalStatus(request.getCriminalStatus());
        criminal.setRiskLevel(request.getRiskLevel());

        Criminal updatedCriminal = criminalRepository.save(criminal);

        return CriminalMapper.toResponse(updatedCriminal);
    }

    @Override
    public void deleteCriminal(Long id) {
        Criminal criminal = criminalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Could Not Find Criminal With Id : " + id));

        criminalRepository.delete(criminal);
    }

    @Override
    public CriminalResponse getByAadhaar(String aadhaarNumber) {
        Criminal criminal = criminalRepository.findByAadhaarNumber(aadhaarNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Could Not Find Criminal With Aadhaar : " + aadhaarNumber));

        return CriminalMapper.toResponse(criminal);
    }

    @Override
    public List<CriminalResponse> searchByName(String name) {
        List<Criminal> criminals = criminalRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(name, name);
        return criminals.stream()
                .map(CriminalMapper::toResponse)
                .collect(Collectors.toList());
    }
}
