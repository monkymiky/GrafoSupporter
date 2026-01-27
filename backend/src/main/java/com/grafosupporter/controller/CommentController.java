package com.grafosupporter.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.grafosupporter.dto.CommentDto;
import com.grafosupporter.dto.CommentRequestDto;
import com.grafosupporter.service.CommentService;
import com.grafosupporter.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/combinations")
public class CommentController {
    private final CommentService commentService;
    private final UserService userService;

    public CommentController(CommentService commentService, UserService userService) {
        this.commentService = commentService;
        this.userService = userService;
    }

    @GetMapping("/{combinationId}/comments")
    public ResponseEntity<List<CommentDto>> getComments(@PathVariable Long combinationId) {
        List<CommentDto> comments = commentService.getCommentsByCombination(combinationId);
        return ResponseEntity.ok(comments);
    }

    @PostMapping("/{combinationId}/comments")
    public ResponseEntity<CommentDto> addComment(
            @PathVariable Long combinationId,
            @Valid @RequestBody CommentRequestDto requestDto,
            Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = authentication.getName();
        var user = userService.findByEmail(email);

        CommentDto comment = commentService.addComment(combinationId, requestDto, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(comment);
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long commentId,
            Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = authentication.getName();
        var user = userService.findByEmail(email);

        commentService.deleteComment(commentId, user);
        return ResponseEntity.noContent().build();
    }
}
