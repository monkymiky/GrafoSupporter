package com.unipd.synclab.grafosupporter.model;

import java.util.List;

import jakarta.persistence.Basic;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GenerationType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.Column;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@NamedEntityGraph(name = "SignCombination.withSignsAndBooks", attributeNodes = { @NamedAttributeNode("signs"),
        @NamedAttributeNode("sourceBook") })
public class SignCombination {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 512)
    private String title;
    @Column(length = 1024)
    private String shortDescription;
    @Column(length = 2048)
    private String longDescription;
    @Column(length = 2048)
    private String OriginalTextCondition;
    @Column(length = 125)
    private String Author;
    @Column(length = 512)
    private String ImagePath;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<ValuatedSign> signs;
    // private Img img;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = true)
    private Book sourceBook;
}
