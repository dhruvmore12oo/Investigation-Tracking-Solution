package com.example.Investigation_Tracking_Solution.dto.fir;

import com.example.Investigation_Tracking_Solution.model.FirStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FirRequest {

    @NotBlank(message = "FIR number is required")
    @Size(max = 50, message = "FIR number cannot exceed 50 characters")
    private String firNumber;

    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title cannot exceed 100 characters")
    private String title;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    @NotBlank(message = "Crime type is required")
    @Size(max = 100, message = "Crime type cannot exceed 100 characters")
    private String crimeType;

    @NotBlank(message = "City is required")
    @Size(max = 50, message = "City cannot exceed 50 characters")
    private String city;

    @NotBlank(message = "Location is required")
    private String location;

    @NotNull(message = "FIR status is required")
    private FirStatus status;

    @NotNull(message = "Assigned officer ID is required")
    private Long officerId;
}
