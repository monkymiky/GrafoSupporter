package com.grafosupporter.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ValuatedSign {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sign_id")
    private Sign sign;

    @ManyToOne
    @JoinColumn(name = "combination_id")
    private Combination combination;

    private Integer max;
    private Integer min;
    private String classification;
    private Boolean isOptional;
}
