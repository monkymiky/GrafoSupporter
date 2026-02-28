package com.grafosupporter.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.grafosupporter.model.User;
import com.grafosupporter.repository.UserRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public User findAndUpdateOrCreateUser(String email, String name, String pictureUrl, String googleId) {
        return userRepository.findByGoogleId(googleId)
                .map(user -> {
                    // Update user info if changed
                    user.setEmail(email);
                    user.setName(name);
                    user.setPictureUrl(pictureUrl);
                    user.setLastLoginAt(LocalDateTime.now());
                    return userRepository.save(user);
                })
                .orElseGet(() -> {
                    // Create new user
                    User newUser = new User(email, name, pictureUrl, googleId);
                    return userRepository.save(newUser);
                });
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found: " + id));
    }
}
