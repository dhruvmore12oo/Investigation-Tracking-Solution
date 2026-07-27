package com.example.Investigation_Tracking_Solution.dto.notification;

import com.example.Investigation_Tracking_Solution.model.NotificationPriority;
import com.example.Investigation_Tracking_Solution.model.NotificationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationRequest {

    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title cannot exceed 100 characters")
    private String title;

    @NotBlank(message = "Message is required")
    @Size(max = 1000, message = "Message cannot exceed 1000 characters")
    private String message;

    @NotNull(message = "Notification type is required")
    private NotificationType notificationType;

    @NotNull(message = "Notification priority is required")
    private NotificationPriority notificationPriority;

    @NotNull(message = "Recipient ID is required")
    private Long recipientId;

    private Long relatedEntityId;

    @Size(max = 50, message = "Related entity type cannot exceed 50 characters")
    private String relatedEntityType;
}
