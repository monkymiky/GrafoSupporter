package com.grafosupporter.dto;

import java.time.LocalDateTime;

import com.grafosupporter.model.NotificationType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDto {
    private Long id;
    private NotificationType type;
    private boolean read;
    private LocalDateTime createdAt;
    private CommentDto comment;
    private CombinationMinimalDto combination;
    private AuthorDto sender;
}
