package com.grafosupporter.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.grafosupporter.dto.NotificationDto;
import com.grafosupporter.service.NotificationService;
import com.grafosupporter.service.UserService;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    private final NotificationService notificationService;
    private final UserService userService;

    public NotificationController(NotificationService notificationService, UserService userService) {
        this.notificationService = notificationService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<NotificationDto>> getUnreadNotifications(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = authentication.getName();
        var user = userService.findByEmail(email);

        List<NotificationDto> notifications = notificationService.getUnreadNotifications(user);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/unread-count")
    public ResponseEntity<Long> getUnreadCount(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = authentication.getName();
        var user = userService.findByEmail(email);

        Long count = notificationService.getUnreadCount(user);
        return ResponseEntity.ok(count);
    }

    @PutMapping("/{notificationId}/read")
    public ResponseEntity<Void> markAsRead(
            @PathVariable Long notificationId,
            Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = authentication.getName();
        var user = userService.findByEmail(email);

        notificationService.markAsRead(notificationId, user);
        return ResponseEntity.noContent().build();
    }
}
