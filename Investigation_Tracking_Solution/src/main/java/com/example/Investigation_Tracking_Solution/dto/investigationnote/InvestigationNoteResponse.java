package com.example.Investigation_Tracking_Solution.dto.investigationnote;

import com.example.Investigation_Tracking_Solution.model.NoteType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class InvestigationNoteResponse {
    private Long id;
    private Long noteId;
    private String title;
    private String note;
    private NoteType noteType;
    private Long investigationId;
    private String investigationNumber;
    private Long createdByUserId;
    private String createdByName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
