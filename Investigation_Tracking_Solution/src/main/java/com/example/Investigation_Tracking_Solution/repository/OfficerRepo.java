package com.example.Investigation_Tracking_Solution.repository;

import com.example.Investigation_Tracking_Solution.model.Officer;
import com.example.Investigation_Tracking_Solution.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    @Query("SELECT o FROM Officer o LEFT JOIN o.user u WHERE LOWER(o.badgeNumber) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(o.department) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(u.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Officer> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Override
    void delete(Officer entity);
}