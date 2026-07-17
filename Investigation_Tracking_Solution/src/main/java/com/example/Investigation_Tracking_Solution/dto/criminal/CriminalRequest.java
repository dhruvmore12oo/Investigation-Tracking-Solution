package com.example.Investigation_Tracking_Solution.dto.criminal;

import com.example.Investigation_Tracking_Solution.model.CriminalStatus;
import com.example.Investigation_Tracking_Solution.model.Gender;
import com.example.Investigation_Tracking_Solution.model.RiskLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CriminalRequest {

    @NotBlank(message = "First name is required")
    @Size(max = 50)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 50)
    private String lastName;

    @NotNull(message = "Gender is required")
    private Gender gender;

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    @NotBlank(message = "Phone number is required")
    @Size(max = 15)
    private String phoneNumber;

    @NotBlank(message = "Aadhaar number is required")
    @Pattern(regexp = "^[2-9]{1}[0-9]{3}[0-9]{4}[0-9]{4}$", message = "Invalid Aadhaar Number")
    private String aadhaarNumber;

    @Size(max = 500)
    private String address;

    @Size(max = 100)
    private String city;

    @Size(max = 100)
    private String state;

    @NotNull(message = "Criminal status is required")
    private CriminalStatus criminalStatus;

    @NotNull(message = "Risk level is required")
    private RiskLevel riskLevel;
}
