package com.unipd.synclab.grafosupporter.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.unipd.synclab.grafosupporter.model.Combination;

public interface SignCombinationRepository
                extends JpaRepository<Combination, Long>, JpaSpecificationExecutor<Combination> {

        @Override
        @EntityGraph(value = "SignCombination.withSignsAndBooks")
        List<Combination> findAll(Specification<Combination> spec);

        @Override
        @EntityGraph(value = "SignCombination.withSignsAndBooks")
        Page<Combination> findAll(Specification<Combination> spec, Pageable pageable);
}
