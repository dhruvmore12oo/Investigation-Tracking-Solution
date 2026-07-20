package com.example.Investigation_Tracking_Solution.mapper;

import com.example.Investigation_Tracking_Solution.dto.fir.FirResponse;
import com.example.Investigation_Tracking_Solution.model.Fir;

public class FirMapper {
    public static FirResponse toResponse(Fir fir) {
        String officerName = null;
        if (fir.getOfficer() != null && fir.getOfficer().getUser() != null) {
            officerName = fir.getOfficer().getUser().getFirstName() + " " + fir.getOfficer().getUser().getLastName();
        }

        return FirResponse.builder()
                .id(fir.getId())
                .firNumber(fir.getFirNumber())
                .title(fir.getTitle())
                .description(fir.getDescription())
                .crimeType(fir.getCrimeType())
                .city(fir.getCity())
                .location(fir.getLocation())
                .status(fir.getStatus())
                .officerId(fir.getOfficer() != null ? fir.getOfficer().getId() : null)
                .officerName(officerName)
                .createdAt(fir.getCreatedAt())
                .build();
    }
}
