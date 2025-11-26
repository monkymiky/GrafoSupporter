package com.grafosupporter.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "contact_feedback")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContactFeedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false, length = 255)
    private String email;

    @Column(nullable = false, length = 50)
    private String category;

    @Column(nullable = false, length = 500)
    private String subject;

    @Column(nullable = false, length = 5000)
    private String message;

    @Column(length = 1000)
    private String userAgent;

    @Column(length = 1000)
    private String pageUrl;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public ContactFeedback(String name, String email, String category, String subject, String message, 
                          String userAgent, String pageUrl) {
        this.name = name;
        this.email = email;
        this.category = category;
        this.subject = subject;
        this.message = message;
        this.userAgent = userAgent;
        this.pageUrl = pageUrl;
        this.createdAt = LocalDateTime.now();
    }
}

