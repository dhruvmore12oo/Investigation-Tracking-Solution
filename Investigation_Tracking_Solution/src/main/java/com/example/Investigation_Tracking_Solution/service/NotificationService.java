package com.example.Investigation_Tracking_Solution.service;

import com.example.Investigation_Tracking_Solution.dto.notification.NotificationRequest;
import com.example.Investigation_Tracking_Solution.dto.notification.NotificationResponse;
import com.example.Investigation_Tracking_Solution.model.NotificationPriority;
import com.example.Investigation_Tracking_Solution.model.NotificationType;
import org.springframework.data.domain.Page;

public interface NotificationService {
    NotificationResponse createNotification(NotificationRequest request);
    NotificationResponse getNotificationById(Long id);
    Page<NotificationResponse> getAllNotifications(int page, int size);
    Page<NotificationResponse> getNotificationsByUser(Long recipientId, int page, int size);
    Page<NotificationResponse> getUnreadNotifications(Long recipientId, int page, int size);
    long getUnreadCount(Long recipientId);
    Page<NotificationResponse> getNotificationsByType(NotificationType notificationType, int page, int size);
    Page<NotificationResponse> getNotificationsByPriority(NotificationPriority notificationPriority, int page, int size);
    NotificationResponse markAsRead(Long id);
    void markAllAsRead(Long recipientId);
    void deleteNotification(Long id);
    Page<NotificationResponse> searchByKeyword(String keyword, int page, int size);
}
