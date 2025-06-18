package com.unipd.synclab.grafosupporter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CombinationDto {

    private Long id;
    private String title;
    private String shortDescription;
    private String longDescription;
    private String originalTextCondition;
    private String author;
    private String imagePath;
    private List<ValuatedSignDto> signs;
    private BookDto sourceBook;
}
