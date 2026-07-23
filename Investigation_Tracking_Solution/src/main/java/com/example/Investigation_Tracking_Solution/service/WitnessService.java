package com.example.Investigation_Tracking_Solution.service;

import com.example.Investigation_Tracking_Solution.dto.witness.WitnessRequest;
import com.example.Investigation_Tracking_Solution.dto.witness.WitnessResponse;
import com.example.Investigation_Tracking_Solution.model.ReliabilityLevel;
import com.example.Investigation_Tracking_Solution.model.WitnessStatus;
import org.springframework.data.domain.Page;

public interface WitnessService {
    WitnessResponse createWitness(WitnessRequest request);
    WitnessResponse getWitnessById(Long id);
    WitnessResponse getByWitnessNumber(String witnessNumber);
    Page<WitnessResponse> getAllWitnesses(int page, int size);
    WitnessResponse updateWitness(Long id, WitnessRequest request);
    void deleteWitness(Long id);
    Page<WitnessResponse> getByCase(Long caseId, int page, int size);
    Page<WitnessResponse> getByStatus(WitnessStatus status, int page, int size);
    Page<WitnessResponse> getByReliabilityLevel(ReliabilityLevel level, int page, int size);
    Page<WitnessResponse> searchByKeyword(String keyword, int page, int size);
}
