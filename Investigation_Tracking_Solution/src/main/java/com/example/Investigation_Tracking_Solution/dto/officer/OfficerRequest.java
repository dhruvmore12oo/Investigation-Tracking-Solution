package com.example.Investigation_Tracking_Solution.dto.officer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class OfficerRequest {
    @NotBlank(message = "BadgeNumber is required")
    @Size(max = 20)
    private String badgeNumber;
    @NotBlank(message = "Rank is required")
    private String rank;
    @NotBlank(message = "Department id required")
    private String department;
    @NotBlank(message = "UserId is required")
    private Long userId;
}
