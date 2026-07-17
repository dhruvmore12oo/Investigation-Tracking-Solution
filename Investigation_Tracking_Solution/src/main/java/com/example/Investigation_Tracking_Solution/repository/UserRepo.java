package com.example.Investigation_Tracking_Solution.repository;

import com.example.Investigation_Tracking_Solution.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    public boolean existsByEmail(String email);
}
