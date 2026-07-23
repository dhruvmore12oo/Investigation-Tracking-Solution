package com.example.Investigation_Tracking_Solution.dto.witness;

import com.example.Investigation_Tracking_Solution.model.Gender;
import com.example.Investigation_Tracking_Solution.model.ReliabilityLevel;
import com.example.Investigation_Tracking_Solution.model.WitnessStatus;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class WitnessRequest {

    @NotBlank(message = "Witness number is required")
    @Size(max = 50, message = "Witness number cannot exceed 50 characters")
    private String witnessNumber;

    @NotBlank(message = "First name is required")
    @Size(max = 50, message = "First name cannot exceed 50 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 50, message = "Last name cannot exceed 50 characters")
    private String lastName;

    private Gender gender;

    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    @NotBlank(message = "Phone number is required")
    @Size(max = 15, message = "Phone number cannot exceed 15 characters")
    @Pattern(regexp = "^[0-9+\\-\\s()]+$", message = "Invalid phone number format")
    private String phoneNumber;

    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email cannot exceed 100 characters")
    private String email;

    @Size(max = 500, message = "Address cannot exceed 500 characters")
    private String address;

    @Size(max = 50, message = "City cannot exceed 50 characters")
    private String city;

    @Size(max = 50, message = "State cannot exceed 50 characters")
    private String state;

    @NotBlank(message = "Statement is required")
    @Size(max = 3000, message = "Statement cannot exceed 3000 characters")
    private String statement;

    @NotNull(message = "Reliability level is required")
    private ReliabilityLevel reliabilityLevel;

    @NotNull(message = "Witness status is required")
    private WitnessStatus witnessStatus;

    @NotNull(message = "Case ID is required")
    private Long caseId;
}
