package com.grafosupporter.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private Long id;
    private String content;
    private AuthorDto author;
    private LocalDateTime createdAt;
    private Long parentCommentId;
    private String parentCommentContent; // Contenuto del commento parent (prime 2 righe)
    private List<CommentDto> replies;
}
