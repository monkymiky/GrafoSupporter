package com.grafosupporter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.grafosupporter.model.VoteType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoteStatsDto {
    private Long upvotes;
    private Long downvotes;
    private Long score; // upvotes - downvotes
    private VoteType userVote; // null if user hasn't voted or is not authenticated
}
