package com.grafosupporter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.grafosupporter.utility.StringTrimDeserializer;

import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCustomUsernameDto {
    @Size(max = 255, message = "Il nome utente personalizzato non può superare i 255 caratteri")
    @JsonDeserialize(using = StringTrimDeserializer.class)
    private String customUsername;
}
