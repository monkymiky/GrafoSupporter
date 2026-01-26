package com.grafosupporter.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.grafosupporter.model.Combination;
import com.grafosupporter.model.User;
import com.grafosupporter.model.Vote;
import com.grafosupporter.model.VoteType;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    Optional<Vote> findByUserAndCombination(User user, Combination combination);
    
    long countByCombinationAndVoteType(Combination combination, VoteType voteType);
    
    void deleteByUserAndCombination(User user, Combination combination);
}
