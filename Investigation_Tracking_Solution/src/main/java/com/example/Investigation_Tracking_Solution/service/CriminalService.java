package com.example.Investigation_Tracking_Solution.service;

import com.example.Investigation_Tracking_Solution.dto.criminal.CriminalRequest;
import com.example.Investigation_Tracking_Solution.dto.criminal.CriminalResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CriminalService {
    CriminalResponse createCriminal(CriminalRequest request);
    CriminalResponse getCriminalById(Long id);
    Page<CriminalResponse> getAllCriminals(int page, int size);
    CriminalResponse updateCriminal(Long id, CriminalRequest request);
    void deleteCriminal(Long id);
    CriminalResponse getByAadhaar(String aadhaarNumber);
    List<CriminalResponse> searchByName(String name);
    Page<CriminalResponse> getByStatus(com.example.Investigation_Tracking_Solution.model.CriminalStatus status, int page, int size);
    Page<CriminalResponse> getByRiskLevel(com.example.Investigation_Tracking_Solution.model.RiskLevel riskLevel, int page, int size);
}
