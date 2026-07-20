package com.example.Investigation_Tracking_Solution.dto.fir;

import com.example.Investigation_Tracking_Solution.model.FirStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class FirResponse {
    private Long id;
    private String firNumber;
    private String title;
    private String description;
    private String crimeType;
    private String city;
    private String location;
    private FirStatus status;
    private Long officerId;
    private String officerName;
    private LocalDateTime createdAt;
}
