package com.example.Investigation_Tracking_Solution.mapper;

import com.example.Investigation_Tracking_Solution.dto.notification.NotificationResponse;
import com.example.Investigation_Tracking_Solution.model.Notification;

public class NotificationMapper {

    public static NotificationResponse toResponse(Notification notification) {
        String recipientName = null;
        if (notification.getRecipient() != null) {
            recipientName = notification.getRecipient().getFirstName() + " "
                    + notification.getRecipient().getLastName();
        }

        return NotificationResponse.builder()
                .id(notification.getId())
                .notificationId(notification.getId())
                .title(notification.getTitle())
                .message(notification.getMessage())
                .notificationType(notification.getNotificationType())
                .notificationPriority(notification.getNotificationPriority())
                .isRead(notification.getIsRead())
                .relatedEntityId(notification.getRelatedEntityId())
                .relatedEntityType(notification.getRelatedEntityType())
                .recipientId(notification.getRecipient() != null ? notification.getRecipient().getId() : null)
                .recipientName(recipientName)
                .createdAt(notification.getCreatedAt())
                .readAt(notification.getReadAt())
                .build();
    }
}
