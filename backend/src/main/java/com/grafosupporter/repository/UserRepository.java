package com.grafosupporter.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.grafosupporter.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByGoogleId(String googleId);
}
