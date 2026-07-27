package com.example.Investigation_Tracking_Solution.service.impl;

import com.example.Investigation_Tracking_Solution.dto.notification.NotificationRequest;
import com.example.Investigation_Tracking_Solution.dto.notification.NotificationResponse;
import com.example.Investigation_Tracking_Solution.exception.ResourceNotFoundException;
import com.example.Investigation_Tracking_Solution.mapper.NotificationMapper;
import com.example.Investigation_Tracking_Solution.model.*;
import com.example.Investigation_Tracking_Solution.repository.NotificationRepo;
import com.example.Investigation_Tracking_Solution.repository.UserRepo;
import com.example.Investigation_Tracking_Solution.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepo notificationRepository;
    private final UserRepo userRepository;

    @Override
    public NotificationResponse createNotification(NotificationRequest request) {
        User recipient = userRepository.findById(request.getRecipientId())
                .orElseThrow(() -> new ResourceNotFoundException("Could Not Find Recipient User With Id : " + request.getRecipientId()));

        Notification notification = Notification.builder()
                .title(request.getTitle())
                .message(request.getMessage())
                .notificationType(request.getNotificationType())
                .notificationPriority(request.getNotificationPriority())
                .recipient(recipient)
                .relatedEntityId(request.getRelatedEntityId())
                .relatedEntityType(request.getRelatedEntityType())
                .isRead(false)
                .build();

        Notification savedNotification = notificationRepository.save(notification);
        return NotificationMapper.toResponse(savedNotification);
    }

    @Override
    public NotificationResponse getNotificationById(Long id) {
        Notification notification = findNotificationById(id);
        validateUserAccess(notification.getRecipient().getId());
        return NotificationMapper.toResponse(notification);
    }

    @Override
    public Page<NotificationResponse> getAllNotifications(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return notificationRepository.findAll(pageable).map(NotificationMapper::toResponse);
    }

    @Override
    public Page<NotificationResponse> getNotificationsByUser(Long recipientId, int page, int size) {
        validateUserAccess(recipientId);
        Pageable pageable = PageRequest.of(page, size);
        return notificationRepository.findByRecipient_Id(recipientId, pageable)
                .map(NotificationMapper::toResponse);
    }

    @Override
    public Page<NotificationResponse> getUnreadNotifications(Long recipientId, int page, int size) {
        validateUserAccess(recipientId);
        Pageable pageable = PageRequest.of(page, size);
        return notificationRepository.findByRecipient_IdAndIsRead(recipientId, false, pageable)
                .map(NotificationMapper::toResponse);
    }

    @Override
    public long getUnreadCount(Long recipientId) {
        validateUserAccess(recipientId);
        return notificationRepository.countByRecipient_IdAndIsReadFalse(recipientId);
    }

    @Override
    public Page<NotificationResponse> getNotificationsByType(NotificationType notificationType, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return notificationRepository.findByNotificationType(notificationType, pageable)
                .map(NotificationMapper::toResponse);
    }

    @Override
    public Page<NotificationResponse> getNotificationsByPriority(NotificationPriority notificationPriority, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return notificationRepository.findByNotificationPriority(notificationPriority, pageable)
                .map(NotificationMapper::toResponse);
    }

    @Override
    public NotificationResponse markAsRead(Long id) {
        Notification notification = findNotificationById(id);
        validateUserAccess(notification.getRecipient().getId());

        if (Boolean.TRUE.equals(notification.getIsRead())) {
            return NotificationMapper.toResponse(notification);
        }

        notification.setIsRead(true);
        notification.setReadAt(LocalDateTime.now());

        Notification updated = notificationRepository.save(notification);
        return NotificationMapper.toResponse(updated);
    }

    @Override
    public void markAllAsRead(Long recipientId) {
        validateUserAccess(recipientId);
        List<Notification> unreadNotifications = notificationRepository.findByRecipient_IdAndIsReadFalse(recipientId);
        if (!unreadNotifications.isEmpty()) {
            LocalDateTime now = LocalDateTime.now();
            unreadNotifications.forEach(n -> {
                n.setIsRead(true);
                n.setReadAt(now);
            });
            notificationRepository.saveAll(unreadNotifications);
        }
    }

    @Override
    public void deleteNotification(Long id) {
        Notification notification = findNotificationById(id);
        User currentUser = getCurrentUser();

        if (currentUser.getRole() != Role.ADMIN) {
            throw new AccessDeniedException("Only ADMIN can delete notifications.");
        }

        notificationRepository.delete(notification);
    }

    @Override
    public Page<NotificationResponse> searchByKeyword(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return notificationRepository.searchByKeyword(keyword, pageable)
                .map(NotificationMapper::toResponse);
    }

    private Notification findNotificationById(Long id) {
        return notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Could Not Find Notification With Id : " + id));
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof User)) {
            throw new AccessDeniedException("Authenticated user is required for this operation.");
        }
        return (User) authentication.getPrincipal();
    }

    private void validateUserAccess(Long recipientId) {
        User currentUser = getCurrentUser();
        if (currentUser.getRole() != Role.ADMIN && !currentUser.getId().equals(recipientId)) {
            throw new AccessDeniedException("You can only access your own notifications.");
        }
    }
}
