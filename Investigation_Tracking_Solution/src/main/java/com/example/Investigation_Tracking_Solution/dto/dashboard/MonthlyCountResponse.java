package com.example.Investigation_Tracking_Solution.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyCountResponse {
    private Integer year;
    private Integer month;
    private Long count;
}
