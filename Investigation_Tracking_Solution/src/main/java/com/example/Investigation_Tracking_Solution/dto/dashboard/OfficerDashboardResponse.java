package com.example.Investigation_Tracking_Solution.dto.dashboard;

import com.example.Investigation_Tracking_Solution.dto.cases.CaseResponse;
import com.example.Investigation_Tracking_Solution.dto.fir.FirResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OfficerDashboardResponse {
    private long assignedFirCount;
    private long assignedCaseCount;
    private long evidenceCollectedCount;
    private List<FirResponse> recentFirs;
    private List<CaseResponse> recentCases;
    private long pendingWorkCount;
    private long unreadNotificationCount;
}
