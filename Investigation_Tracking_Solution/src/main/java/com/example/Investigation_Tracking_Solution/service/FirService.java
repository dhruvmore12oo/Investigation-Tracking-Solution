package com.example.Investigation_Tracking_Solution.service;

import com.example.Investigation_Tracking_Solution.dto.fir.FirRequest;
import com.example.Investigation_Tracking_Solution.dto.fir.FirResponse;
import com.example.Investigation_Tracking_Solution.model.FirStatus;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

public interface FirService {
    FirResponse createFir(FirRequest request);
    FirResponse getFirById(Long id);
    Page<FirResponse> getAllFirs(int page, int size);
    FirResponse updateFir(Long id, FirRequest request);
    void deleteFir(Long id);
    FirResponse getByFirNumber(String firNumber);
    Page<FirResponse> getByStatus(FirStatus status, int page, int size);
    Page<FirResponse> getByOfficerId(Long officerId, int page, int size);
    Page<FirResponse> getByDateRange(LocalDateTime startDate, LocalDateTime endDate, int page, int size);
    Page<FirResponse> searchByKeyword(String keyword, int page, int size);
}
