package com.example.Investigation_Tracking_Solution.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "case_criminal")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CaseCriminal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "case_criminal_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "case_id", nullable = false)
    private Case caseId;

    @ManyToOne
    @JoinColumn(name = "criminal_id", nullable = false)
    private Criminal criminalId;

    @Column(name = "role_in_case", length = 100)
    private String roleInCase;

    @Enumerated(EnumType.STRING)
    @Column(name = "arrest_status", nullable = false)
    private ArrestStatus arrestStatus;

    @Column(name = "added_at", nullable = false, updatable = false)
    private LocalDateTime addedAt;

    @PrePersist
    public void prePersist() {
        if (addedAt == null) {
            addedAt = LocalDateTime.now();
        }
    }
}
