package com.unipd.synclab.grafosupporter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.unipd.synclab.grafosupporter.model.SignCombination;

public interface SignCombinationRepository
        extends JpaRepository<SignCombination, Long>, JpaSpecificationExecutor<SignCombination> {

}
