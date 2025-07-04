package com.unipd.synclab.grafosupporter.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.unipd.synclab.grafosupporter.model.Combination;

public interface CombinationRepository
                extends JpaRepository<Combination, Long>, JpaSpecificationExecutor<Combination> {

        @Override
        @EntityGraph(value = "Combination.withSignsAndBooks")
        @org.springframework.lang.NonNull
        List<Combination> findAll(@org.springframework.lang.Nullable Specification<Combination> spec);

        @Override
        @EntityGraph(value = "Combination.withSignsAndBooks")
        @org.springframework.lang.NonNull
        Page<Combination> findAll(@org.springframework.lang.Nullable Specification<Combination> spec,
                        @org.springframework.lang.Nullable Pageable pageable);
}
