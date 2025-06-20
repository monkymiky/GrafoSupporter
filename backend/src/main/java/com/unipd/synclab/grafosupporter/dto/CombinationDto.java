package com.unipd.synclab.grafosupporter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.unipd.synclab.grafosupporter.utility.StringTrimDeserializer;

import jakarta.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CombinationDto {

    private Long id;
    @NotEmpty
    @JsonDeserialize(using = StringTrimDeserializer.class)
    private String title;
    @NotEmpty
    @JsonDeserialize(using = StringTrimDeserializer.class)
    private String shortDescription;
    @JsonDeserialize(using = StringTrimDeserializer.class)
    private String longDescription;
    private String originalTextCondition;
    private String author;
    private String imagePath;
    private List<ValuatedSignDto> signs;
    private BookDto sourceBook;
}
