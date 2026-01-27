package com.grafosupporter.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.grafosupporter.model.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    
    @Query("SELECT n FROM Notification n WHERE n.recipient.id = :userId AND n.read = false ORDER BY n.createdAt DESC")
    List<Notification> findUnreadNotificationsByUserId(@Param("userId") Long userId);
    
    @Query("SELECT COUNT(n) FROM Notification n WHERE n.recipient.id = :userId AND n.read = false")
    Long countUnreadNotificationsByUserId(@Param("userId") Long userId);
}
