package com.grafosupporter.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.grafosupporter.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
}
