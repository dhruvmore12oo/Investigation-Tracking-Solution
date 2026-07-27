package com.example.Investigation_Tracking_Solution.controller;

import com.example.Investigation_Tracking_Solution.dto.notification.NotificationRequest;
import com.example.Investigation_Tracking_Solution.dto.notification.NotificationResponse;
import com.example.Investigation_Tracking_Solution.model.NotificationPriority;
import com.example.Investigation_Tracking_Solution.model.NotificationType;
import com.example.Investigation_Tracking_Solution.model.User;
import com.example.Investigation_Tracking_Solution.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<NotificationResponse> createNotification(@Valid @RequestBody NotificationRequest request) {
        return new ResponseEntity<>(notificationService.createNotification(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OFFICER', 'INVESTIGATOR')")
    public ResponseEntity<NotificationResponse> getNotificationById(@PathVariable Long id) {
        return ResponseEntity.ok(notificationService.getNotificationById(id));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<NotificationResponse>> getAllNotifications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(notificationService.getAllNotifications(page, size));
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OFFICER', 'INVESTIGATOR')")
    public ResponseEntity<Page<NotificationResponse>> getNotificationsByUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(notificationService.getNotificationsByUser(userId, page, size));
    }

    @GetMapping("/unread")
    @PreAuthorize("hasAnyRole('ADMIN', 'OFFICER', 'INVESTIGATOR')")
    public ResponseEntity<Page<NotificationResponse>> getUnreadNotifications(
            @AuthenticationPrincipal User currentUser,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(notificationService.getUnreadNotifications(currentUser.getId(), page, size));
    }

    @GetMapping("/unread-count")
    @PreAuthorize("hasAnyRole('ADMIN', 'OFFICER', 'INVESTIGATOR')")
    public ResponseEntity<Long> getUnreadCount(@AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(notificationService.getUnreadCount(currentUser.getId()));
    }

    @GetMapping("/type/{type}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OFFICER', 'INVESTIGATOR')")
    public ResponseEntity<Page<NotificationResponse>> getNotificationsByType(
            @PathVariable NotificationType type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(notificationService.getNotificationsByType(type, page, size));
    }

    @GetMapping("/priority/{priority}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OFFICER', 'INVESTIGATOR')")
    public ResponseEntity<Page<NotificationResponse>> getNotificationsByPriority(
            @PathVariable NotificationPriority priority,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(notificationService.getNotificationsByPriority(priority, page, size));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'OFFICER', 'INVESTIGATOR')")
    public ResponseEntity<Page<NotificationResponse>> searchNotifications(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(notificationService.searchByKeyword(keyword, page, size));
    }

    @PutMapping("/{id}/read")
    @PreAuthorize("hasAnyRole('ADMIN', 'OFFICER', 'INVESTIGATOR')")
    public ResponseEntity<NotificationResponse> markAsRead(@PathVariable Long id) {
        return ResponseEntity.ok(notificationService.markAsRead(id));
    }

    @PutMapping("/read-all")
    @PreAuthorize("hasAnyRole('ADMIN', 'OFFICER', 'INVESTIGATOR')")
    public ResponseEntity<Void> markAllAsRead(@AuthenticationPrincipal User currentUser) {
        notificationService.markAllAsRead(currentUser.getId());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.noContent().build();
    }
}
