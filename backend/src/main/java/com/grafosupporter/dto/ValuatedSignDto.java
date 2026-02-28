package com.grafosupporter.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValuatedSignDto {
    private Long signId;
    private Integer max;
    private Integer min;
    private String classification;
    private Boolean isOptional;
    private String name;
    private String temperamento;
}
