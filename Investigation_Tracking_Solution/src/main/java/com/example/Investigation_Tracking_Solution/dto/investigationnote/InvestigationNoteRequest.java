package com.example.Investigation_Tracking_Solution.dto.investigationnote;

import com.example.Investigation_Tracking_Solution.model.NoteType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvestigationNoteRequest {

    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title cannot exceed 100 characters")
    private String title;

    @NotBlank(message = "Note content is required")
    @Size(max = 5000, message = "Note content cannot exceed 5000 characters")
    private String note;

    @NotNull(message = "Note type is required")
    private NoteType noteType;

    @NotNull(message = "Investigation ID is required")
    private Long investigationId;
}
