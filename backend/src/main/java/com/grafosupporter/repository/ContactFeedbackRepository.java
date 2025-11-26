package com.grafosupporter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.grafosupporter.model.ContactFeedback;

@Repository
public interface ContactFeedbackRepository extends JpaRepository<ContactFeedback, Long> {
}

