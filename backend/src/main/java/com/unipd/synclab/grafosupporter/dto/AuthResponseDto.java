package com.unipd.synclab.grafosupporter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDto {
    private String token;
    private String email;
    private String name;
    private String pictureUrl;
    private Long userId;
}



