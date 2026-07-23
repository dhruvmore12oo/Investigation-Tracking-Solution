package com.example.Investigation_Tracking_Solution.mapper;

import com.example.Investigation_Tracking_Solution.dto.witness.WitnessResponse;
import com.example.Investigation_Tracking_Solution.model.Witness;

public class WitnessMapper {

    public static WitnessResponse toResponse(Witness witness) {
        return WitnessResponse.builder()
                .id(witness.getId())
                .witnessNumber(witness.getWitnessNumber())
                .firstName(witness.getFirstName())
                .lastName(witness.getLastName())
                .gender(witness.getGender())
                .dateOfBirth(witness.getDateOfBirth())
                .phoneNumber(witness.getPhoneNumber())
                .email(witness.getEmail())
                .address(witness.getAddress())
                .city(witness.getCity())
                .state(witness.getState())
                .statement(witness.getStatement())
                .reliabilityLevel(witness.getReliabilityLevel())
                .witnessStatus(witness.getWitnessStatus())
                .caseId(witness.getWitnessCase() != null ? witness.getWitnessCase().getId() : null)
                .caseNumber(witness.getWitnessCase() != null ? witness.getWitnessCase().getCaseNumber() : null)
                .createdAt(witness.getCreatedAt())
                .updatedAt(witness.getUpdatedAt())
                .build();
    }
}
