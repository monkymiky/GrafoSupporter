package com.grafosupporter.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.grafosupporter.dto.AuthorDto;
import com.grafosupporter.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/authors")
@RequiredArgsConstructor
public class AuthorController {
    private final UserService userService;

    @GetMapping("/search")
    public ResponseEntity<List<AuthorDto>> searchAuthors(@RequestParam String prefix) {
        if (prefix == null || prefix.trim().isEmpty()) {
            return ResponseEntity.ok(List.of());
        }
        
        List<AuthorDto> authors = userService.findAuthorsByCustomUsernamePrefix(prefix);
        return ResponseEntity.ok(authors);
    }
}
