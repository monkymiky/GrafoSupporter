package com.grafosupporter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoteRequestDto {
    private String voteType; // "UP", "DOWN", or null to remove vote
}
