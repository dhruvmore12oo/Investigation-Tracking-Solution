package com.example.Investigation_Tracking_Solution.dto.notification;

import com.example.Investigation_Tracking_Solution.model.NotificationPriority;
import com.example.Investigation_Tracking_Solution.model.NotificationType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class NotificationResponse {
    private Long id;
    private Long notificationId;
    private String title;
    private String message;
    private NotificationType notificationType;
    private NotificationPriority notificationPriority;
    private Boolean isRead;
    private Long relatedEntityId;
    private String relatedEntityType;
    private Long recipientId;
    private String recipientName;
    private LocalDateTime createdAt;
    private LocalDateTime readAt;
}
