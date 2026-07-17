package com.example.Investigation_Tracking_Solution.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "investigation_notes")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvestigationNote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "note_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "investigation_id", nullable = false)
    private Investigation investigationId;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User authorId;

    @Column(name = "note_text", nullable = false, length = 2000)
    private String noteText;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}