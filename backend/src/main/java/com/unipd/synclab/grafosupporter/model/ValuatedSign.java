package com.unipd.synclab.grafosupporter.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class ValuatedSign {
    private Long signId;
    private Integer max;
    private Integer min;
    private String classification;
    private Boolean isOptional;
}
