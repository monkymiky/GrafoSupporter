package com.grafosupporter.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.grafosupporter.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    
    @Query("SELECT c FROM Comment c WHERE c.combination.id = :combinationId AND c.parentComment IS NULL ORDER BY c.createdAt ASC")
    List<Comment> findTopLevelCommentsByCombinationId(@Param("combinationId") Long combinationId);
    
    @Query("SELECT c FROM Comment c WHERE c.parentComment.id = :parentCommentId ORDER BY c.createdAt ASC")
    List<Comment> findRepliesByParentCommentId(@Param("parentCommentId") Long parentCommentId);
    
    @Query("SELECT c FROM Comment c LEFT JOIN FETCH c.parentComment WHERE c.combination.id = :combinationId ORDER BY c.createdAt ASC")
    List<Comment> findAllCommentsByCombinationId(@Param("combinationId") Long combinationId);
}
