package com.grafosupporter.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.grafosupporter.dto.ContactFeedbackDto;
import com.grafosupporter.model.ContactFeedback;
import com.grafosupporter.repository.ContactFeedbackRepository;

@Service
@RequiredArgsConstructor
public class ContactFeedbackService {
    private final ContactFeedbackRepository contactFeedbackRepository;
    private final FeedbackNotificationService feedbackNotificationService;

    @Transactional
    public ContactFeedback saveFeedback(ContactFeedbackDto feedbackDto) {
        ContactFeedback feedback = new ContactFeedback(
                feedbackDto.getName(),
                feedbackDto.getEmail(),
                feedbackDto.getCategory(),
                feedbackDto.getSubject(),
                feedbackDto.getMessage(),
                feedbackDto.getUserAgent(),
                feedbackDto.getPageUrl()
        );
        ContactFeedback saved = contactFeedbackRepository.save(feedback);
        feedbackNotificationService.notifyNewFeedback(saved);
        return saved;
    }
}

