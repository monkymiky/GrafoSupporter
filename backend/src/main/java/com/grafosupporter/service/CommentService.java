package com.grafosupporter.service;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.grafosupporter.dto.CommentDto;
import com.grafosupporter.dto.CommentRequestDto;
import com.grafosupporter.model.Comment;
import com.grafosupporter.model.Combination;
import com.grafosupporter.model.User;
import com.grafosupporter.repository.CommentRepository;
import com.grafosupporter.repository.CombinationRepository;
import com.grafosupporter.utility.CommentMapper;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final CombinationRepository combinationRepository;
    private final CommentMapper commentMapper;
    private final NotificationService notificationService;

    public CommentService(CommentRepository commentRepository, 
                         CombinationRepository combinationRepository,
                         CommentMapper commentMapper,
                         NotificationService notificationService) {
        this.commentRepository = commentRepository;
        this.combinationRepository = combinationRepository;
        this.commentMapper = commentMapper;
        this.notificationService = notificationService;
    }

    @Transactional(readOnly = true)
    public List<CommentDto> getCommentsByCombination(Long combinationId) {
        Combination combination = combinationRepository.findById(combinationId)
                .orElseThrow(() -> new EntityNotFoundException("Combination not found with id: " + combinationId));

        List<Comment> allComments = commentRepository.findAllCommentsByCombinationId(combinationId);
        
        return allComments.stream()
                .map(this::toFlatCommentDto)
                .collect(Collectors.toList());
    }

    private CommentDto toFlatCommentDto(Comment comment) {
        CommentDto dto = commentMapper.toCommentDto(comment);
        
        if (comment.getParentComment() != null) {
            String parentContent = comment.getParentComment().getContent();
            String[] lines = parentContent.split("\n");
            if (lines.length > 2) {
                dto.setParentCommentContent(lines[0] + "\n" + lines[1] + "...");
            } else {
                dto.setParentCommentContent(parentContent);
            }
        }
        
        return dto;
    }

    @Transactional
    public CommentDto addComment(Long combinationId, CommentRequestDto requestDto, User author) {
        Combination combination = combinationRepository.findById(combinationId)
                .orElseThrow(() -> new EntityNotFoundException("Combination not found with id: " + combinationId));

        Comment parentComment = null;
        if (requestDto.getParentCommentId() != null) {
            parentComment = commentRepository.findById(requestDto.getParentCommentId())
                    .orElseThrow(() -> new EntityNotFoundException("Parent comment not found with id: " + requestDto.getParentCommentId()));
            
            if (!parentComment.getCombination().getId().equals(combinationId)) {
                throw new InvalidParameterException("Parent comment does not belong to this combination");
            }
        }

        Comment newComment = new Comment(requestDto.getContent(), author, combination, parentComment);
        Comment savedComment = commentRepository.save(newComment);

        if (parentComment != null) {
            if (!parentComment.getAuthor().getId().equals(author.getId())) {
                notificationService.createNotification(
                    com.grafosupporter.model.NotificationType.REPLY_TO_COMMENT,
                    parentComment.getAuthor(),
                    savedComment,
                    combination
                );
            }
            
            if (!combination.getAuthor().getId().equals(author.getId()) 
                    && !combination.getAuthor().getId().equals(parentComment.getAuthor().getId())) {
                notificationService.createNotification(
                    com.grafosupporter.model.NotificationType.REPLY_TO_COMMENT,
                    combination.getAuthor(),
                    savedComment,
                    combination
                );
            }
        } else {
            if (!combination.getAuthor().getId().equals(author.getId())) {
                notificationService.createNotification(
                    com.grafosupporter.model.NotificationType.COMMENT_ON_COMBINATION,
                    combination.getAuthor(),
                    savedComment,
                    combination
                );
            }
        }

        return commentMapper.toCommentDto(savedComment);
    }

    @Transactional
    public void deleteComment(Long commentId, User user) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found with id: " + commentId));

        if (!comment.getAuthor().getId().equals(user.getId())) {
            throw new InvalidParameterException("Non puoi eliminare commenti di altri utenti");
        }

        commentRepository.delete(comment);
    }
}
