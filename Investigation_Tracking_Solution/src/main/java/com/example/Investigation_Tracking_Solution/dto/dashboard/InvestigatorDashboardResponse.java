package com.example.Investigation_Tracking_Solution.dto.dashboard;

import com.example.Investigation_Tracking_Solution.dto.investigationnote.InvestigationNoteResponse;
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
public class InvestigatorDashboardResponse {
    private long assignedInvestigationsCount;
    private long completedInvestigationsCount;
    private long pendingInvestigationsCount;
    private long evidenceInAssignedInvestigationsCount;
    private long witnessesInAssignedCasesCount;
    private List<InvestigationNoteResponse> recentInvestigationNotes;
    private long unreadNotificationCount;
}
