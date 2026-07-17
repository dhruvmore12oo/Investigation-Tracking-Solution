package com.example.Investigation_Tracking_Solution.model;

import jakarta.persistence.*;
import lombok.*;

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

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Column(nullable = false, length = 15)
    private String phone;

    @Column(unique = true, length = 100)
    private String email;

    @Column(length = 500)
    private String address;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}