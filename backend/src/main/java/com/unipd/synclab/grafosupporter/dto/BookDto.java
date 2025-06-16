package com.unipd.synclab.grafosupporter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {
    private Long id;
    private String title;
    private String writer;
    private Integer pubblicationJear;
    private String editor;
    private String isbn;
}
