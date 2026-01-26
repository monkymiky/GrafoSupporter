package com.grafosupporter.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.grafosupporter.dto.AuthorDto;
import com.grafosupporter.model.User;
import com.grafosupporter.repository.CombinationRepository;
import com.grafosupporter.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final CombinationRepository combinationRepository;

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

    @Transactional
    public User updateCustomUsername(Long userId, String customUsername) {
        User user = findById(userId);
        user.setCustomUsername(customUsername);
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public List<AuthorDto> findAuthorsByCustomUsernamePrefix(String prefix) {
        if (prefix == null || prefix.trim().isEmpty()) {
            return List.of();
        }

        String trimmedPrefix = prefix.trim();
        
        List<User> usersByCustomUsername = userRepository.findByCustomUsernameStartingWithIgnoreCase(trimmedPrefix);
        List<User> usersByName = userRepository.findByNameStartingWithIgnoreCase(trimmedPrefix);
        
        Set<Long> usersWithCustomUsername = usersByCustomUsername.stream()
                .map(User::getId)
                .collect(Collectors.toSet());
        
        List<User> usersWithoutCustomUsername = usersByName.stream()
                .filter(user -> user.getCustomUsername() == null 
                        || user.getCustomUsername().trim().isEmpty())
                .toList();
        
        Set<Long> allUserIds = new java.util.HashSet<>();
        usersByCustomUsername.forEach(user -> allUserIds.add(user.getId()));
        usersWithoutCustomUsername.forEach(user -> allUserIds.add(user.getId()));
        
        if (allUserIds.isEmpty()) {
            return List.of();
        }

        Set<Long> authorIds = combinationRepository.findAll().stream()
                .filter(combination -> combination.getAuthor() != null 
                        && allUserIds.contains(combination.getAuthor().getId()))
                .map(combination -> combination.getAuthor().getId())
                .collect(Collectors.toSet());

        Set<Long> processedUserIds = new java.util.HashSet<>();
        List<AuthorDto> authors = new java.util.ArrayList<>();
        
        for (User user : usersByCustomUsername) {
            if (authorIds.contains(user.getId()) && !processedUserIds.contains(user.getId())) {
                String authorName = user.getCustomUsername() != null 
                        && !user.getCustomUsername().trim().isEmpty()
                        ? user.getCustomUsername()
                        : user.getName();
                authors.add(new AuthorDto(user.getId(), authorName, user.getPictureUrl()));
                processedUserIds.add(user.getId());
            }
        }
        
        for (User user : usersWithoutCustomUsername) {
            if (authorIds.contains(user.getId()) && !processedUserIds.contains(user.getId())) {
                authors.add(new AuthorDto(user.getId(), user.getName(), user.getPictureUrl()));
                processedUserIds.add(user.getId());
            }
        }

        return authors.stream()
                .limit(15)
                .collect(Collectors.toList());
    }
}
