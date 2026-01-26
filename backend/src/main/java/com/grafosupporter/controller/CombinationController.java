package com.grafosupporter.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.grafosupporter.dto.CombinationDto;
import com.grafosupporter.dto.CombinationSearchRequestDto;
import com.grafosupporter.dto.VoteRequestDto;
import com.grafosupporter.dto.VoteStatsDto;
import com.grafosupporter.model.Combination;
import com.grafosupporter.model.VoteType;
import com.grafosupporter.service.CombinationService;
import com.grafosupporter.service.UserService;
import com.grafosupporter.service.VoteService;
import com.grafosupporter.utility.CombinationMapper;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/combinations")
public class CombinationController {
    private final CombinationService combinationService;
    private final CombinationMapper combinationMapper;
    private final VoteService voteService;
    private final UserService userService;

    public CombinationController(CombinationService combinationService, CombinationMapper combinationMapper,
            VoteService voteService, UserService userService) {
        this.combinationService = combinationService;
        this.combinationMapper = combinationMapper;
        this.voteService = voteService;
        this.userService = userService;
    }

    @PostMapping("/search")
    public ResponseEntity<List<CombinationDto>> getCombinations(@RequestBody CombinationSearchRequestDto request) {
        Map<Long, Integer> searchedSign = request.getSearchedSign();
        List<String> authorCustomUsernames = request.getAuthorCustomUsernames();
        
        List<CombinationDto> combinations = combinationService.getCombinations(searchedSign, authorCustomUsernames);
        return ResponseEntity.ok(combinations);
    }

    @PostMapping
    public ResponseEntity<CombinationDto> addCombination(@Valid @RequestBody CombinationDto combinationDto) {
        Combination savedCombination = combinationService
                .addCombination(combinationMapper.toCombinationEntity(combinationDto));
        CombinationDto savedCombinationDto = combinationMapper.toCombinationResponseDto(savedCombination);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedCombination.getId())
                .toUri();
        return ResponseEntity.created(location).body(savedCombinationDto);
    }

    @PutMapping("/{combinationId}")
    public ResponseEntity<CombinationDto> editCombination(
            @PathVariable Long combinationId,
            @Valid @RequestBody CombinationDto combinationDto) throws IOException {
        Combination combinationToUpdate = combinationMapper.toCombinationEntity(combinationDto);
        Combination updatedCombination = combinationService.editCombination(combinationId, combinationToUpdate);
        CombinationDto updatedDto = combinationMapper.toCombinationResponseDto(updatedCombination);
        return ResponseEntity.ok(updatedDto);
    }

    @DeleteMapping("/{combinationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCombination(@PathVariable Long combinationId) {
        combinationService.deleteCombination(combinationId);
    }

    @PostMapping("/{combinationId}/vote")
    public ResponseEntity<VoteStatsDto> voteCombination(
            @PathVariable Long combinationId,
            @RequestBody VoteRequestDto voteRequest,
            Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }

        String email = authentication.getName();
        var user = userService.findByEmail(email);
        Long userId = user.getId();

        VoteType voteType = null;
        if (voteRequest.getVoteType() != null && !voteRequest.getVoteType().trim().isEmpty()) {
            String voteTypeStr = voteRequest.getVoteType().trim().toUpperCase();
            if ("UP".equals(voteTypeStr)) {
                voteType = VoteType.UP;
            } else if ("DOWN".equals(voteTypeStr)) {
                voteType = VoteType.DOWN;
            }
        }

        VoteStatsDto stats = voteService.vote(combinationId, userId, voteType);
        return ResponseEntity.ok(stats);
    }

}
