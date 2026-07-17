package com.example.Investigation_Tracking_Solution.repository;

import com.example.Investigation_Tracking_Solution.model.Officer;
import com.example.Investigation_Tracking_Solution.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OfficerRepo extends JpaRepository<Officer, Long> {

    Optional<Officer> findByBadgeNumber(String badgeNumber);

    Optional<Officer> findByUser_Id(Long userId);

    List<Officer> findByDepartment(String department);

    Optional<Officer> findById(Long id);

    List<Officer> findAll();

    Boolean existsByBadgeNumber(String badgeNumber);

    Boolean existsByUser(User user);

    @Override
    void delete(Officer entity);
}