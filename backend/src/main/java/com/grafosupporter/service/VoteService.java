package com.grafosupporter.service;

import java.security.InvalidParameterException;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.grafosupporter.dto.VoteStatsDto;
import com.grafosupporter.model.Combination;
import com.grafosupporter.model.User;
import com.grafosupporter.model.Vote;
import com.grafosupporter.model.VoteType;
import com.grafosupporter.repository.CombinationRepository;
import com.grafosupporter.repository.UserRepository;
import com.grafosupporter.repository.VoteRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class VoteService {
    private final VoteRepository voteRepository;
    private final CombinationRepository combinationRepository;
    private final UserRepository userRepository;

    public VoteService(VoteRepository voteRepository, CombinationRepository combinationRepository,
            UserRepository userRepository) {
        this.voteRepository = voteRepository;
        this.combinationRepository = combinationRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public VoteStatsDto getVoteStats(Long combinationId, Long userId) {
        Combination combination = combinationRepository.findById(combinationId)
                .orElseThrow(() -> new EntityNotFoundException("Combination not found with id: " + combinationId));

        long upvotes = voteRepository.countByCombinationAndVoteType(combination, VoteType.UP);
        long downvotes = voteRepository.countByCombinationAndVoteType(combination, VoteType.DOWN);
        long score = upvotes - downvotes;

        VoteType userVote = null;
        if (userId != null) {
            User user = userRepository.findById(userId)
                    .orElse(null);
            if (user != null) {
                userVote = voteRepository.findByUserAndCombination(user, combination)
                        .map(Vote::getVoteType)
                        .orElse(null);
            }
        }

        return new VoteStatsDto(upvotes, downvotes, score, userVote);
    }

    @Transactional
    public VoteStatsDto vote(Long combinationId, Long userId, VoteType voteType) {
        if (userId == null) {
            throw new InvalidParameterException("User ID is required to vote");
        }

        Combination combination = combinationRepository.findById(combinationId)
                .orElseThrow(() -> new EntityNotFoundException("Combination not found with id: " + combinationId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        if (combination.getAuthor() != null && combination.getAuthor().getId().equals(userId)) {
            throw new InvalidParameterException("Non puoi votare le tue combinazioni");
        }

        Optional<Vote> existingVote = voteRepository.findByUserAndCombination(user, combination);

        if (voteType == null) {
            if (existingVote.isPresent()) {
                voteRepository.delete(existingVote.get());
            }
        } else {
            if (existingVote.isPresent()) {
                Vote vote = existingVote.get();
                if (vote.getVoteType() == voteType) {
                    voteRepository.delete(vote);
                } else {
                    vote.setVoteType(voteType);
                    voteRepository.save(vote);
                }
            } else {
                Vote newVote = new Vote(user, combination, voteType);
                voteRepository.save(newVote);
            }
        }

        return getVoteStats(combinationId, userId);
    }
}
