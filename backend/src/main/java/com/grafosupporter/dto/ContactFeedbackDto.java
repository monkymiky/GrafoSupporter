package com.grafosupporter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.grafosupporter.utility.StringTrimDeserializer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactFeedbackDto {

    @NotBlank(message = "Il nome è obbligatorio")
    @Size(max = 255, message = "Il nome non può superare i 255 caratteri")
    @JsonDeserialize(using = StringTrimDeserializer.class)
    private String name;

    @NotBlank(message = "L'email è obbligatoria")
    @Email(message = "L'email deve essere valida")
    @Size(max = 255, message = "L'email non può superare i 255 caratteri")
    @JsonDeserialize(using = StringTrimDeserializer.class)
    private String email;

    @NotNull(message = "La categoria è obbligatoria")
    @Pattern(regexp = "bug|suggestion|question|other", message = "La categoria deve essere: bug, suggestion, question o other")
    @JsonDeserialize(using = StringTrimDeserializer.class)
    private String category;

    @NotBlank(message = "L'oggetto è obbligatorio")
    @Size(max = 500, message = "L'oggetto non può superare i 500 caratteri")
    @JsonDeserialize(using = StringTrimDeserializer.class)
    private String subject;

    @NotBlank(message = "Il messaggio è obbligatorio")
    @Size(max = 5000, message = "Il messaggio non può superare i 5000 caratteri")
    @JsonDeserialize(using = StringTrimDeserializer.class)
    private String message;

    @Size(max = 1000, message = "User agent non può superare i 1000 caratteri")
    private String userAgent;

    @Size(max = 1000, message = "Page URL non può superare i 1000 caratteri")
    private String pageUrl;
}

