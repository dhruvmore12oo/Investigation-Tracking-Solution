package com.example.Investigation_Tracking_Solution.repository;

import com.example.Investigation_Tracking_Solution.model.Notification;
import com.example.Investigation_Tracking_Solution.model.NotificationPriority;
import com.example.Investigation_Tracking_Solution.model.NotificationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationRepo extends JpaRepository<Notification, Long> {

    Page<Notification> findByRecipient_Id(Long recipientId, Pageable pageable);

    Page<Notification> findByRecipient_IdAndIsRead(Long recipientId, Boolean isRead, Pageable pageable);

    List<Notification> findByRecipient_IdAndIsReadFalse(Long recipientId);

    Page<Notification> findByNotificationType(NotificationType notificationType, Pageable pageable);

    Page<Notification> findByNotificationPriority(NotificationPriority notificationPriority, Pageable pageable);

    long countByRecipient_IdAndIsReadFalse(Long recipientId);

    @Query("SELECT n FROM Notification n LEFT JOIN n.recipient r WHERE " +
           "LOWER(n.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(n.message) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(r.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(r.lastName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Notification> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT n FROM Notification n WHERE n.recipient.id = :userId AND (" +
           "LOWER(n.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(n.message) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Notification> searchByRecipientAndKeyword(@Param("userId") Long userId, @Param("keyword") String keyword, Pageable pageable);
}
