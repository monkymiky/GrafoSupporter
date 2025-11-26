package com.grafosupporter.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.grafosupporter.dto.ContactFeedbackDto;
import com.grafosupporter.service.ContactFeedbackService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/contact")
public class ContactController {
    private final ContactFeedbackService contactFeedbackService;

    public ContactController(ContactFeedbackService contactFeedbackService) {
        this.contactFeedbackService = contactFeedbackService;
    }

    @PostMapping
    public ResponseEntity<Void> submitFeedback(@Valid @RequestBody ContactFeedbackDto feedbackDto) {
        contactFeedbackService.saveFeedback(feedbackDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
