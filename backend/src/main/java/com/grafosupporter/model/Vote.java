package com.grafosupporter.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "vote", uniqueConstraints = {
    @UniqueConstraint(columnNames = { "user_id", "combination_id" })
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "combination_id", nullable = false)
    private Combination combination;

    @Enumerated(EnumType.STRING)
    @jakarta.persistence.Column(nullable = false)
    private VoteType voteType;

    private LocalDateTime createdAt;

    public Vote(User user, Combination combination, VoteType voteType) {
        this.user = user;
        this.combination = combination;
        this.voteType = voteType;
        this.createdAt = LocalDateTime.now();
    }
}
