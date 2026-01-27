package com.grafosupporter.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.grafosupporter.dto.AuthorDto;
import com.grafosupporter.dto.CommentDto;
import com.grafosupporter.dto.CombinationMinimalDto;
import com.grafosupporter.dto.NotificationDto;
import com.grafosupporter.model.Comment;
import com.grafosupporter.model.Combination;
import com.grafosupporter.model.Notification;
import com.grafosupporter.model.NotificationType;
import com.grafosupporter.model.User;
import com.grafosupporter.repository.NotificationRepository;
import com.grafosupporter.utility.CommentMapper;

import jakarta.persistence.EntityNotFoundException;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final CommentMapper commentMapper;

    public NotificationService(NotificationRepository notificationRepository, CommentMapper commentMapper) {
        this.notificationRepository = notificationRepository;
        this.commentMapper = commentMapper;
    }

    @Transactional
    public void createNotification(NotificationType type, User recipient, Comment comment, Combination combination) {
        Notification notification = new Notification(type, recipient, comment, combination);
        notificationRepository.save(notification);
    }

    @Transactional(readOnly = true)
    public List<NotificationDto> getUnreadNotifications(User user) {
        List<Notification> notifications = notificationRepository.findUnreadNotificationsByUserId(user.getId());
        
        return notifications.stream()
                .map(this::toNotificationDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void markAsRead(Long notificationId, User user) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new EntityNotFoundException("Notification not found with id: " + notificationId));

        if (!notification.getRecipient().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Non puoi segnare come letta una notifica di un altro utente");
        }

        notification.setRead(true);
        notificationRepository.save(notification);
    }

    @Transactional(readOnly = true)
    public Long getUnreadCount(User user) {
        return notificationRepository.countUnreadNotificationsByUserId(user.getId());
    }

    private NotificationDto toNotificationDto(Notification notification) {
        NotificationDto dto = new NotificationDto();
        dto.setId(notification.getId());
        dto.setType(notification.getType());
        dto.setRead(notification.isRead());
        dto.setCreatedAt(notification.getCreatedAt());

        if (notification.getComment() != null) {
            CommentDto commentDto = commentMapper.toCommentDto(notification.getComment());
            dto.setComment(commentDto);
            
            if (notification.getComment().getAuthor() != null) {
                String authorName = notification.getComment().getAuthor().getCustomUsername() != null 
                        && !notification.getComment().getAuthor().getCustomUsername().trim().isEmpty()
                        ? notification.getComment().getAuthor().getCustomUsername()
                        : notification.getComment().getAuthor().getName();
                AuthorDto senderDto = new AuthorDto(
                    notification.getComment().getAuthor().getId(),
                    authorName,
                    notification.getComment().getAuthor().getPictureUrl()
                );
                dto.setSender(senderDto);
            }
        }

        if (notification.getCombination() != null) {
            CombinationMinimalDto combinationDto = new CombinationMinimalDto(
                notification.getCombination().getId(),
                notification.getCombination().getTitle()
            );
            dto.setCombination(combinationDto);
        }

        return dto;
    }
}
