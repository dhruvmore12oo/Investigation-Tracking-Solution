package com.example.Investigation_Tracking_Solution.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "witnesses")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Witness {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "witness_id")
    private Long id;

    @Column(name = "witness_number", nullable = false, unique = true, length = 50)
    private String witnessNumber;

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Gender gender;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "phone_number", nullable = false, unique = true, length = 15)
    private String phoneNumber;

    @Column(unique = true, length = 100)
    private String email;

    @Column(length = 500)
    private String address;

    @Column(length = 50)
    private String city;

    @Column(length = 50)
    private String state;

    @Column(nullable = false, length = 3000)
    private String statement;

    @Enumerated(EnumType.STRING)
    @Column(name = "reliability_level", nullable = false)
    private ReliabilityLevel reliabilityLevel;

    @Enumerated(EnumType.STRING)
    @Column(name = "witness_status", nullable = false)
    private WitnessStatus witnessStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "case_id", nullable = false)
    private Case witnessCase;

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