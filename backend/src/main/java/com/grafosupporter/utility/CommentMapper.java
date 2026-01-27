package com.grafosupporter.utility;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.grafosupporter.dto.AuthorDto;
import com.grafosupporter.dto.CommentDto;
import com.grafosupporter.model.Comment;

@Component
public class CommentMapper {
    
    public CommentDto toCommentDto(Comment comment) {
        if (comment == null) {
            return null;
        }

        CommentDto dto = new CommentDto();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setCreatedAt(comment.getCreatedAt());
        
        if (comment.getParentComment() != null) {
            dto.setParentCommentId(comment.getParentComment().getId());
        }
        
        if (comment.getAuthor() != null) {
            String authorName = comment.getAuthor().getCustomUsername() != null 
                    && !comment.getAuthor().getCustomUsername().trim().isEmpty()
                    ? comment.getAuthor().getCustomUsername()
                    : comment.getAuthor().getName();
            AuthorDto authorDto = new AuthorDto(
                comment.getAuthor().getId(),
                authorName,
                comment.getAuthor().getPictureUrl()
            );
            dto.setAuthor(authorDto);
        }
        
        
        return dto;
    }
}
