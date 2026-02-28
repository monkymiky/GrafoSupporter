package com.grafosupporter.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.grafosupporter.model.ValuatedSign;

public interface ValuatedSignRepository extends JpaRepository<ValuatedSign, Long> {

}
