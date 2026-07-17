package com.example.Investigation_Tracking_Solution.dto.criminal;

import com.example.Investigation_Tracking_Solution.model.CriminalStatus;
import com.example.Investigation_Tracking_Solution.model.Gender;
import com.example.Investigation_Tracking_Solution.model.RiskLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class CriminalResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private Gender gender;
    private LocalDate dateOfBirth;
    private String phoneNumber;
    private String aadhaarNumber;
    private String address;
    private String city;
    private String state;
    private CriminalStatus criminalStatus;
    private RiskLevel riskLevel;
    private LocalDateTime createdAt;
}
