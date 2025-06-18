package com.unipd.synclab.grafosupporter.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unipd.synclab.grafosupporter.model.ValuatedSign;

public interface ValuatedSignRepository extends JpaRepository<ValuatedSign, Long> {

}
