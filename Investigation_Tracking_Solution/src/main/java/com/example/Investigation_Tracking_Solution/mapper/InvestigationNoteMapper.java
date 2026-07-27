package com.example.Investigation_Tracking_Solution.mapper;

import com.example.Investigation_Tracking_Solution.dto.investigationnote.InvestigationNoteResponse;
import com.example.Investigation_Tracking_Solution.model.InvestigationNote;

public class InvestigationNoteMapper {

    public static InvestigationNoteResponse toResponse(InvestigationNote investigationNote) {
        String authorName = null;
        if (investigationNote.getCreatedBy() != null) {
            authorName = investigationNote.getCreatedBy().getFirstName() + " "
                    + investigationNote.getCreatedBy().getLastName();
        }

        return InvestigationNoteResponse.builder()
                .id(investigationNote.getId())
                .noteId(investigationNote.getId())
                .title(investigationNote.getTitle())
                .note(investigationNote.getNote())
                .noteType(investigationNote.getNoteType())
                .investigationId(investigationNote.getInvestigation() != null ? investigationNote.getInvestigation().getId() : null)
                .investigationNumber(investigationNote.getInvestigation() != null ? investigationNote.getInvestigation().getInvestigationNumber() : null)
                .createdByUserId(investigationNote.getCreatedBy() != null ? investigationNote.getCreatedBy().getId() : null)
                .createdByName(authorName)
                .createdAt(investigationNote.getCreatedAt())
                .updatedAt(investigationNote.getUpdatedAt())
                .build();
    }
}
