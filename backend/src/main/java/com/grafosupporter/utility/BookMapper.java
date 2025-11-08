package com.grafosupporter.utility;

import org.springframework.stereotype.Component;

import com.grafosupporter.dto.BookDto;
import com.grafosupporter.model.Book;

@Component
public class BookMapper {
    Book toBookEntity(BookDto dto) {
        return new Book(
                dto.getId(),
                dto.getTitle(),
                dto.getWriter(),
                dto.getPublicationYear(),
                dto.getEditor(),
                dto.getIsbn());
    }

    BookDto toBookDto(Book book) {
        return new BookDto(
                book.getId(),
                book.getTitle(),
                book.getWriter(),
                book.getPublicationYear(),
                book.getEditor(),
                book.getIsbn());
    }
}
