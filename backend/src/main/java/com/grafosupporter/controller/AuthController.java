package com.grafosupporter.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import com.grafosupporter.dto.AuthResponseDto;
import com.grafosupporter.dto.UpdateCustomUsernameDto;
import com.grafosupporter.service.UserService;
import com.grafosupporter.utility.JwtUtil;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @Value("${app.frontend.base-url}")
    private String frontendBaseUrl;

    @GetMapping("/oauth2/success")
    public ResponseEntity<Void> oauth2Success(
            @AuthenticationPrincipal OAuth2User oauth2User) {

        if (oauth2User == null) {
            return ResponseEntity.status(302)
                    .header("Location", frontendBaseUrl + "/?error=auth_failed")
                    .build();
        }

        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("name");
        String pictureUrl = oauth2User.getAttribute("picture");
        String googleId = oauth2User.getAttribute("sub");

        if (email == null || googleId == null) {
            return ResponseEntity.status(302)
                    .header("Location", frontendBaseUrl + "/?error=auth_failed")
                    .build();
        }

        var user = userService.findAndUpdateOrCreateUser(email, name, pictureUrl, googleId);
        String token = jwtUtil.generateToken(user.getEmail(), user.getId());

        String redirectUrl = String.format(
                "%s/?token=%s&email=%s&name=%s&pictureUrl=%s&userId=%d&customUsername=%s",
                frontendBaseUrl,
                token,
                java.net.URLEncoder.encode(user.getEmail(), java.nio.charset.StandardCharsets.UTF_8),
                user.getName() != null
                        ? java.net.URLEncoder.encode(user.getName(), java.nio.charset.StandardCharsets.UTF_8)
                        : "",
                user.getPictureUrl() != null
                        ? java.net.URLEncoder.encode(user.getPictureUrl(), java.nio.charset.StandardCharsets.UTF_8)
                        : "",
                user.getId(),
                user.getCustomUsername() != null
                        ? java.net.URLEncoder.encode(user.getCustomUsername(), java.nio.charset.StandardCharsets.UTF_8)
                        : "");

        return ResponseEntity.status(302)
                .header("Location", redirectUrl)
                .build();
    }

    @GetMapping("/login")
    public ResponseEntity<String> login() {
        return ResponseEntity.ok("Please authenticate via OAuth2");
    }

    @GetMapping("/me")
    public ResponseEntity<AuthResponseDto> getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }

        String email = authentication.getName();
        var user = userService.findByEmail(email);

        AuthResponseDto response = new AuthResponseDto(
                null, // Don't return token on /me endpoint
                user.getEmail(),
                user.getName(),
                user.getPictureUrl(),
                user.getCustomUsername(),
                user.getId());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/validate")
    public ResponseEntity<Map<String, Object>> validateToken(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body(Map.of("valid", false));
        }

        try {
            String token = authHeader.substring(7);
            String email = jwtUtil.getEmailFromToken(token);

            if (jwtUtil.validateToken(token, email)) {
                Long userId = jwtUtil.getUserIdFromToken(token);
                return ResponseEntity.ok(Map.of(
                        "valid", true,
                        "email", email,
                        "userId", userId));
            } else {
                return ResponseEntity.status(401).body(Map.of("valid", false));
            }
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("valid", false));
        }
    }

    @PutMapping("/profile/username")
    public ResponseEntity<Map<String, Object>> updateCustomUsername(
            @Valid @RequestBody UpdateCustomUsernameDto dto,
            Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }

        String email = authentication.getName();
        var user = userService.findByEmail(email);
        
        String customUsername = dto.getCustomUsername();
        if (customUsername != null && customUsername.trim().isEmpty()) {
            customUsername = null;
        }
        
        var updatedUser = userService.updateCustomUsername(user.getId(), customUsername);

        Map<String, Object> body = new HashMap<>();
        body.put("message", "Custom username aggiornato con successo");
        body.put("customUsername", updatedUser.getCustomUsername());
        return ResponseEntity.ok(body);
    }
}
