package com.example.Investigation_Tracking_Solution.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@Setter
@Table(name = "criminals")
@NoArgsConstructor
@AllArgsConstructor
public class Criminal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "criminal_id")
    private Long id;

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Column(name = "dob", nullable = false)
    private LocalDate dateOfBirth;

    @Column(name = "phone_number", unique = true, length = 15)
    private String phoneNumber;

    @Column(name = "aadhaar_number", unique = true, length = 12)
    private String aadhaarNumber;

    @Column(length = 500)
    private String address;

    @Column(length = 100)
    private String city;

    @Column(length = 100)
    private String state;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CriminalStatus criminalStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "risk_level" ,nullable = false)
    private RiskLevel riskLevel;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if(createdAt == null) this.createdAt = LocalDateTime.now();
    }
}
