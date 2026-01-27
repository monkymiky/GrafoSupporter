package com.grafosupporter.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.grafosupporter.utility.StringTrimDeserializer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequestDto {
    @NotBlank(message = "Il contenuto del commento è obbligatorio")
    @Size(max = 2000, message = "Il commento non può superare i 2000 caratteri")
    @JsonDeserialize(using = StringTrimDeserializer.class)
    private String content;

    private Long parentCommentId;
}
