package com.example.Investigation_Tracking_Solution.service;

import com.example.Investigation_Tracking_Solution.dto.officer.OfficerRequest;
import com.example.Investigation_Tracking_Solution.dto.officer.OfficerResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OfficerService {
    OfficerResponse createOfficer(OfficerRequest request);
    OfficerResponse getOfficerById(Long id);
    Page<OfficerResponse> getAllOfficers(int page, int size);
    OfficerResponse updateOfficer(Long id, OfficerRequest request);
    OfficerResponse getOfficerByBadgeNumber(String badgeNumber);
    void deleteOfficer(Long id);
}
