package com.example.Investigation_Tracking_Solution.dto.officer;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class OfficerResponse {
    private Long id;
    private String badgeNumber;
    private String rank;
    private String department;
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;

}
