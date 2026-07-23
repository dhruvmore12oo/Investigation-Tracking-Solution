package com.example.Investigation_Tracking_Solution.dto.witness;

import com.example.Investigation_Tracking_Solution.model.Gender;
import com.example.Investigation_Tracking_Solution.model.ReliabilityLevel;
import com.example.Investigation_Tracking_Solution.model.WitnessStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class WitnessResponse {
    private Long id;
    private String witnessNumber;
    private String firstName;
    private String lastName;
    private Gender gender;
    private LocalDate dateOfBirth;
    private String phoneNumber;
    private String email;
    private String address;
    private String city;
    private String state;
    private String statement;
    private ReliabilityLevel reliabilityLevel;
    private WitnessStatus witnessStatus;
    private Long caseId;
    private String caseNumber;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
