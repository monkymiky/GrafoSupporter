package com.unipd.synclab.grafosupporter.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unipd.synclab.grafosupporter.model.Combination;
import com.unipd.synclab.grafosupporter.model.Sign;
import com.unipd.synclab.grafosupporter.model.ValuatedSign;

public interface ValuatedSignRepository extends JpaRepository<ValuatedSign, Long> {
    Optional<ValuatedSign> findBySignAndCombination(Sign sign, Combination combination);
}
