package com.unipd.synclab.grafosupporter.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unipd.synclab.grafosupporter.model.Sign;

public interface SignRepository extends JpaRepository<Sign,Long> {
    Optional<Sign> findByName(String name);
}
