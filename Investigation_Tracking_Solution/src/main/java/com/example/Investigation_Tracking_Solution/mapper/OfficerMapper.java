package com.example.Investigation_Tracking_Solution.mapper;

import com.example.Investigation_Tracking_Solution.dto.officer.OfficerResponse;
import com.example.Investigation_Tracking_Solution.model.Officer;

public class OfficerMapper {
    public static OfficerResponse toResponse(Officer officer) {
        return OfficerResponse.builder()
                .id(officer.getId())
                .badgeNumber(officer.getBadgeNumber())
                .rank(officer.getRank())
                .department(officer.getDepartment())
                .userId(officer.getUser().getId())
                .firstName(officer.getUser().getFirstName())
                .lastName(officer.getUser().getLastName())
                .email(officer.getUser().getEmail())
                .build();
    }

}

