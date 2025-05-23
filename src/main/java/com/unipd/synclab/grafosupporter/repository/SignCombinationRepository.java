package com.unipd.synclab.grafosupporter.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.unipd.synclab.grafosupporter.model.SignCombination;

public interface SignCombinationRepository
                extends JpaRepository<SignCombination, Long>, JpaSpecificationExecutor<SignCombination> {

        @Override
        @EntityGraph(value = "SignCombination.withSignsAndBooks")
        List<SignCombination> findAll(Specification<SignCombination> spec);

        @Override
        @EntityGraph(value = "SignCombination.withSignsAndBooks")
        Page<SignCombination> findAll(Specification<SignCombination> spec, Pageable pageable);
}
