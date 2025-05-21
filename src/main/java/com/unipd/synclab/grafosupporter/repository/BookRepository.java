package com.unipd.synclab.grafosupporter.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.unipd.synclab.grafosupporter.model.Book;

public interface BookRepository extends JpaRepository<Book,Long> {}
