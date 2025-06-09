package com.unipd.synclab.grafosupporter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import com.unipd.synclab.grafosupporter.model.Book;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CombinationResponseDto {

    private Long id;
    private String title;
    private String shortDescription;
    private String longDescription;
    private String OriginalTextCondition;
    private String Author;
    private String ImagePath;
    private List<ValuatedSignDto> signs;
    private Book sourceBook;
}
