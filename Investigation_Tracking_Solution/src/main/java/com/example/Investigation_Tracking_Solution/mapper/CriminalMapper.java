package com.example.Investigation_Tracking_Solution.mapper;

import com.example.Investigation_Tracking_Solution.dto.criminal.CriminalResponse;
import com.example.Investigation_Tracking_Solution.model.Criminal;

public class CriminalMapper {
    public static CriminalResponse toResponse(Criminal criminal) {
        return CriminalResponse.builder()
                .id(criminal.getId())
                .firstName(criminal.getFirstName())
                .lastName(criminal.getLastName())
                .gender(criminal.getGender())
                .dateOfBirth(criminal.getDateOfBirth())
                .phoneNumber(criminal.getPhoneNumber())
                .aadhaarNumber(criminal.getAadhaarNumber())
                .address(criminal.getAddress())
                .city(criminal.getCity())
                .state(criminal.getState())
                .criminalStatus(criminal.getCriminalStatus())
                .riskLevel(criminal.getRiskLevel())
                .createdAt(criminal.getCreatedAt())
                .build();
    }
}
