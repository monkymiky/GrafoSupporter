package com.grafosupporter.service;

import java.time.format.DateTimeFormatter;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.grafosupporter.model.ContactFeedback;

@Service
public class FeedbackNotificationService {

    private static final Logger log = LoggerFactory.getLogger(FeedbackNotificationService.class);
    private static final String TELEGRAM_SEND_MESSAGE_URL = "https://api.telegram.org/bot%s/sendMessage";
    private static final int TELEGRAM_MAX_MESSAGE_LENGTH = 4096;
    private static final String TRUNCATE_SUFFIX = "\n\n(… messaggio troncato)";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final RestTemplate restTemplate;

    @Value("${app.feedback-notification.telegram.bot-token:}")
    private String botToken;

    @Value("${app.feedback-notification.telegram.chat-id:}")
    private String chatId;

    public FeedbackNotificationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void notifyNewFeedback(ContactFeedback feedback) {
        if (botToken == null || botToken.isBlank() || chatId == null || chatId.isBlank()) {
            log.debug("Notifica Telegram feedback disabilitata: bot-token o chat-id non configurati");
            return;
        }

        String text = buildMessageText(feedback);
        if (text.length() > TELEGRAM_MAX_MESSAGE_LENGTH) {
            int maxContent = TELEGRAM_MAX_MESSAGE_LENGTH - TRUNCATE_SUFFIX.length();
            text = text.substring(0, maxContent) + TRUNCATE_SUFFIX;
        }

        String url = TELEGRAM_SEND_MESSAGE_URL.formatted(botToken.trim());
        Map<String, Object> body = Map.of(
                "chat_id", chatId.trim(),
                "text", text
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        try {
            var response = restTemplate.postForEntity(url, entity, String.class);
            if (!response.getStatusCode().is2xxSuccessful()) {
                log.warn("Telegram sendMessage risposta non 2xx: {} - body: {}",
                        response.getStatusCode(), response.getBody());
            }
        } catch (Exception e) {
            log.warn("Errore invio notifica Telegram per feedback: {}", e.getMessage());
        }
    }

    private static String buildMessageText(ContactFeedback feedback) {
        StringBuilder sb = new StringBuilder();
        sb.append("Nuovo feedback da Grafosupporter\n\n");
        sb.append("Nome: ").append(nullToEmpty(feedback.getName())).append("\n");
        sb.append("Email: ").append(nullToEmpty(feedback.getEmail())).append("\n");
        sb.append("Categoria: ").append(nullToEmpty(feedback.getCategory())).append("\n");
        sb.append("Oggetto: ").append(nullToEmpty(feedback.getSubject())).append("\n\n");
        sb.append("Messaggio:\n").append(nullToEmpty(feedback.getMessage())).append("\n\n");
        sb.append("Pagina: ").append(nullToEmpty(feedback.getPageUrl())).append("\n");
        if (feedback.getCreatedAt() != null) {
            sb.append("Data: ").append(feedback.getCreatedAt().format(DATE_FORMATTER)).append("\n");
        }
        if (feedback.getUserAgent() != null && !feedback.getUserAgent().isBlank()) {
            sb.append("User-Agent: ").append(feedback.getUserAgent()).append("\n");
        }
        return sb.toString();
    }

    private static String nullToEmpty(String s) {
        return s != null ? s : "";
    }
}
