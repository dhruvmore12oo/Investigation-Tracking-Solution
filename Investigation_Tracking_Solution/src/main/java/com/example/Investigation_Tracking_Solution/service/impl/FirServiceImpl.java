package com.example.Investigation_Tracking_Solution.service.impl;

import com.example.Investigation_Tracking_Solution.dto.fir.FirRequest;
import com.example.Investigation_Tracking_Solution.dto.fir.FirResponse;
import com.example.Investigation_Tracking_Solution.exception.BadRequestException;
import com.example.Investigation_Tracking_Solution.exception.ResourceNotFoundException;
import com.example.Investigation_Tracking_Solution.mapper.FirMapper;
import com.example.Investigation_Tracking_Solution.model.Fir;
import com.example.Investigation_Tracking_Solution.model.FirStatus;
import com.example.Investigation_Tracking_Solution.model.Officer;
import com.example.Investigation_Tracking_Solution.repository.FirRepo;
import com.example.Investigation_Tracking_Solution.repository.OfficerRepo;
import com.example.Investigation_Tracking_Solution.service.FirService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class FirServiceImpl implements FirService {

    private final FirRepo firRepository;
    private final OfficerRepo officerRepository;

    @Override
    public FirResponse createFir(FirRequest request) {
        if (firRepository.findByFirNumber(request.getFirNumber()).isPresent()) {
            throw new BadRequestException("FIR number already exists.");
        }

        Officer officer = officerRepository.findById(request.getOfficerId())
                .orElseThrow(() -> new ResourceNotFoundException("Could Not Find Officer With Id : " + request.getOfficerId()));

        Fir fir = Fir.builder()
                .firNumber(request.getFirNumber())
                .title(request.getTitle())
                .description(request.getDescription())
                .crimeType(request.getCrimeType())
                .city(request.getCity())
                .location(request.getLocation())
                .status(request.getStatus())
                .officer(officer)
                .build();

        Fir savedFir = firRepository.save(fir);
        return FirMapper.toResponse(savedFir);
    }

    @Override
    public FirResponse getFirById(Long id) {
        Fir fir = firRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Could Not Find FIR With Id : " + id));
        return FirMapper.toResponse(fir);
    }

    @Override
    public Page<FirResponse> getAllFirs(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return firRepository.findAll(pageable).map(FirMapper::toResponse);
    }

    @Override
    public FirResponse updateFir(Long id, FirRequest request) {
        Fir fir = firRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Could Not Find FIR With Id : " + id));

        if (!fir.getFirNumber().equals(request.getFirNumber()) &&
                firRepository.findByFirNumber(request.getFirNumber()).isPresent()) {
            throw new BadRequestException("FIR number already exists.");
        }

        Officer officer = officerRepository.findById(request.getOfficerId())
                .orElseThrow(() -> new ResourceNotFoundException("Could Not Find Officer With Id : " + request.getOfficerId()));

        fir.setFirNumber(request.getFirNumber());
        fir.setTitle(request.getTitle());
        fir.setDescription(request.getDescription());
        fir.setCrimeType(request.getCrimeType());
        fir.setCity(request.getCity());
        fir.setLocation(request.getLocation());
        fir.setStatus(request.getStatus());
        fir.setOfficer(officer);

        Fir updatedFir = firRepository.save(fir);
        return FirMapper.toResponse(updatedFir);
    }

    @Override
    public void deleteFir(Long id) {
        Fir fir = firRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Could Not Find FIR With Id : " + id));
        firRepository.delete(fir);
    }

    @Override
    public FirResponse getByFirNumber(String firNumber) {
        Fir fir = firRepository.findByFirNumber(firNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Could Not Find FIR With Number : " + firNumber));
        return FirMapper.toResponse(fir);
    }

    @Override
    public Page<FirResponse> getByStatus(FirStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return firRepository.findByStatus(status, pageable).map(FirMapper::toResponse);
    }

    @Override
    public Page<FirResponse> getByOfficerId(Long officerId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return firRepository.findByOfficerId(officerId, pageable).map(FirMapper::toResponse);
    }

    @Override
    public Page<FirResponse> getByDateRange(LocalDateTime startDate, LocalDateTime endDate, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return firRepository.findByCreatedAtBetween(startDate, endDate, pageable).map(FirMapper::toResponse);
    }

    @Override
    public Page<FirResponse> searchByKeyword(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return firRepository.searchByKeyword(keyword, pageable).map(FirMapper::toResponse);
    }
}
