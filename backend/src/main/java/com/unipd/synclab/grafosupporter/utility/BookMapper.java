package com.unipd.synclab.grafosupporter.utility;

import org.springframework.stereotype.Component;

import com.unipd.synclab.grafosupporter.dto.BookDto;
import com.unipd.synclab.grafosupporter.model.Book;

@Component
public class BookMapper {
    Book toBookEntity(BookDto dto) {
        return new Book(
                dto.getId(),
                dto.getTitle(),
                dto.getWriter(),
                dto.getPubblicationJear(),
                dto.getEditor(),
                dto.getIsbn());
    }

    BookDto toBookDto(Book book) {
        return new BookDto(
                book.getId(),
                book.getTitle(),
                book.getWriter(),
                book.getPubblicationJear(),
                book.getEditor(),
                book.getIsbn());
    }
}
