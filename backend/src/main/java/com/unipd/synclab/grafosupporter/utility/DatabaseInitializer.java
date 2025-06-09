package com.unipd.synclab.grafosupporter.utility;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.unipd.synclab.grafosupporter.model.Book;
import com.unipd.synclab.grafosupporter.model.Sign;
import com.unipd.synclab.grafosupporter.model.SignCombination;
import com.unipd.synclab.grafosupporter.model.ValuatedSign;
import com.unipd.synclab.grafosupporter.repository.BookRepository;
import com.unipd.synclab.grafosupporter.repository.SignCombinationRepository;
import com.unipd.synclab.grafosupporter.repository.SignRepository;

import java.util.ArrayList;

import java.util.List;
import java.util.Optional;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import org.springframework.beans.factory.annotation.Autowired;

import lombok.Data;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

@Data
class JsonSign {
        private Long id;
        private Integer min;
        private Integer max;
        private Boolean optional;
        private String classification;
}

@Data
class JsonCombination {
        private String title;
        private String description_short;
        private String description_long;
        private String original_text_condition;
        private String author;
        private Long source;
        private String imagePath;
        private List<JsonSign> signs;
}

@Data
class JsonRoot {
        private List<JsonCombination> combinazioni;
}

@Component
public class DatabaseInitializer {
        @Autowired
        BookRepository bookRepository;
        @Autowired
        SignRepository signRepository;
        @Autowired
        SignCombinationRepository signCombinationRepository;

        private Book processBook(Long id, String combinationTitle) {
                Optional<Book> book = bookRepository.findById(id);
                if (book.isPresent()) {
                        return book.get();
                } else {
                        System.err.println("ATTENZIONE: Libro con id '" + id
                                        + "' non trovato nel database. Impossibile creare la combinazione :"
                                        + combinationTitle);
                }
                return null;
        }

        public void populateDatabaseFromJson(String file) {
                if (signCombinationRepository.count() > 0) {
                        System.out.println("SignCombinationDatabase already initialized.");
                        return;
                }

                ObjectMapper objectMapper = new ObjectMapper();
                try {

                        JsonRoot root = objectMapper.readValue(file, JsonRoot.class);

                        List<SignCombination> combinations = new ArrayList<>();

                        for (JsonCombination jc : root.getCombinazioni()) {
                                List<ValuatedSign> valuatedSigns = new ArrayList<>();
                                for (JsonSign js : jc.getSigns()) {
                                        Optional<Sign> optionalSign = signRepository.findById(js.getId());
                                        if (optionalSign.isPresent()) {
                                                Sign foundSign = optionalSign.get();
                                                ValuatedSign vs = new ValuatedSign();
                                                vs.setSignId(foundSign.getId());
                                                vs.setMin(js.getMin());
                                                vs.setMax(js.getMax());
                                                vs.setIsOptional(js.getOptional());
                                                vs.setClassification(js.getClassification());
                                                valuatedSigns.add(vs);
                                        } else {
                                                System.err.println("ATTENZIONE: Segno con id " + js.getId()
                                                                + " non trovato per combinazione " + jc.getTitle());
                                        }
                                }

                                com.unipd.synclab.grafosupporter.model.Book book = processBook(jc.getSource(),
                                                jc.getTitle());

                                SignCombination sc = new SignCombination();
                                sc.setTitle(jc.getTitle());
                                sc.setShortDescription(jc.getDescription_short());
                                sc.setLongDescription(jc.getDescription_long());
                                sc.setOriginalTextCondition(jc.getOriginal_text_condition());
                                sc.setAuthor(jc.getAuthor());
                                sc.setImagePath(jc.getImagePath());
                                sc.setSigns(valuatedSigns);
                                sc.setSourceBook(book);

                                combinations.add(sc);
                        }

                        signCombinationRepository.saveAll(combinations);
                        System.out.println("SignCombinationDatabase initialized from JSON.");
                } catch (IOException e) {
                        System.err.println("Errore nella lettura/parsing del file JSON: " + e.getMessage());
                }
        }

        @EventListener(ApplicationReadyEvent.class)
        public void initBookDatabase() throws Exception {
                if (bookRepository.count() == 0) {
                        ArrayList<Book> books = new ArrayList<Book>();
                        books.add(new Book(null, "Manuale di grafologia", "Girolamo Moretti", 1914,
                                        "Edizioni Messaggero Padova",
                                        "9788825010008"));
                        books.add(new Book(null, "Vizio. Psicologia e grafologia dei sette vizi capitali",
                                        "Girolamo Moretti", 1937,
                                        "Edizioni Messaggero Padova", "9788825010015"));
                        books.add(new Book(null, "Trattato scientifico di perizie grafiche su base grafologica",
                                        "Girolamo Moretti",
                                        1942, "Edizioni Messaggero Padova", "9788825010251"));
                        books.add(new Book(null, "Grafologia somatica. Il corpo umano dalla scrittura",
                                        "Girolamo Moretti", 1945,
                                        "Edizioni Messaggero Padova", "9788825010022"));
                        books.add(new Book(null, "Grafologia pedagogica", "Girolamo Moretti", 1947,
                                        "Edizioni Messaggero Padova",
                                        "9788825010268"));
                        books.add(new Book(null, "Grafologia delle attitudini umane", "Girolamo Moretti", 1948,
                                        "Edizioni Messaggero Padova", "9788825010039"));
                        books.add(new Book(null, "I santi dalla scrittura", "Girolamo Moretti", 1952,
                                        "Edizioni San Paolo",
                                        "9788821535727"));
                        books.add(new Book(null, "Trattato di grafologia. Intelligenza, sentimento", "Girolamo Moretti",
                                        1955,
                                        "Edizioni Messaggero Padova", "9788825010800"));
                        books.add(new Book(null, "Scompensi, anomalie della psiche e grafologia", "Girolamo Moretti",
                                        1962,
                                        "Edizioni Messaggero Padova", "9788825010046"));
                        books.add(new Book(null, "La passione predominante. Grafologia differenziale",
                                        "Girolamo Moretti", 1962,
                                        "Edizioni Messaggero Padova", "9788825010053"));
                        books.add(new Book(null, "Analisi grafologiche", "Girolamo Moretti", 1966,
                                        "Edizioni Messaggero Padova",
                                        "9788825010060"));
                        books.add(new Book(null, "I grandi dalla scrittura", "Girolamo Moretti", 1966,
                                        "Edizioni Messaggero Padova",
                                        "9788825010077"));
                        books.add(new Book(null, "Grafologia e pedagogia nella scuola dell'obbligo", "Girolamo Moretti",
                                        1970,
                                        "Edizioni Messaggero Padova", "9788825010084"));
                        books.add(new Book(null, "Chi lo avrebbe mai pensato. Autobiografia", "Girolamo Moretti", 1977,
                                        "Edizioni Messaggero Padova", "9788825010091"));

                        bookRepository.saveAll(books);
                        System.out.println("BookDatabase initialized with default Books.");
                } else {
                        System.out.println("BookDatabase already initialized.");
                }

                if (signRepository.count() == 0) {
                        ArrayList<Sign> signs = new ArrayList<Sign>();
                        signs.add(new Sign(null, "largaTraLettere", null));
                        // temperamento Cessione
                        signs.add(new Sign(null, "curva", "Cessione"));
                        signs.add(new Sign(null, "discendente", "Cessione"));
                        signs.add(new Sign(null, "titubante", "Cessione"));
                        signs.add(new Sign(null, "aperturaACapoDelleOeA", "Cessione"));
                        signs.add(new Sign(null, "asteColConcavoADestra", "Cessione"));
                        signs.add(new Sign(null, "pendente", "Cessione"));
                        signs.add(new Sign(null, "profusa", "Cessione"));
                        signs.add(new Sign(null, "fluida", "Cessione"));
                        signs.add(new Sign(null, "attaccata", "Cessione"));
                        // temperamento Resistenza
                        signs.add(new Sign(null, "angoliB", "Resistenza"));
                        signs.add(new Sign(null, "mantieneIlRigo", "Resistenza"));
                        signs.add(new Sign(null, "secca", "Resistenza"));
                        signs.add(new Sign(null, "asteRette", "Resistenza"));
                        signs.add(new Sign(null, "dritta", "Resistenza"));
                        signs.add(new Sign(null, "recisa", "Resistenza"));
                        signs.add(new Sign(null, "austera", "Resistenza"));
                        signs.add(new Sign(null, "Chiara", "Resistenza"));
                        // temperamento Assalto
                        signs.add(new Sign(null, "angoliA", "Assalto"));
                        signs.add(new Sign(null, "intozzata1Modo", "Assalto"));
                        signs.add(new Sign(null, "ascendente", "Assalto"));
                        signs.add(new Sign(null, "scattante", "Assalto"));
                        signs.add(new Sign(null, "asteColConcavoASinistra", "Assalto"));
                        signs.add(new Sign(null, "ardita", "Assalto"));
                        signs.add(new Sign(null, "slanciata", "Assalto"));
                        signs.add(new Sign(null, "impaziente", "Assalto"));
                        signs.add(new Sign(null, "spavalda", "Assalto"));
                        signs.add(new Sign(null, "acuta", "Assalto"));
                        signs.add(new Sign(null, "veloce", "Assalto"));
                        signs.add(new Sign(null, "solenne", "Assalto"));
                        // temperamento Attesa
                        signs.add(new Sign(null, "intozzata2Modo", "Attesa"));
                        signs.add(new Sign(null, "contorta", "Attesa"));
                        signs.add(new Sign(null, "sinuosa", "Attesa"));
                        signs.add(new Sign(null, "stentata", "Attesa"));
                        signs.add(new Sign(null, "tentennante", "Attesa"));
                        signs.add(new Sign(null, "ponderata", "Attesa"));
                        signs.add(new Sign(null, "calma", "Attesa"));
                        signs.add(new Sign(null, "filiforme", "Attesa"));
                        signs.add(new Sign(null, "fine", "Attesa"));
                        signs.add(new Sign(null, "grossa", "Attesa"));
                        signs.add(new Sign(null, "grossolana", "Attesa"));
                        signs.add(new Sign(null, "ricciNascondimento", "Attesa"));
                        signs.add(new Sign(null, "ricciAmmanieramento", "Attesa"));
                        signs.add(new Sign(null, "ricciMitomania", "Attesa"));
                        signs.add(new Sign(null, "vezzosa", "Attesa"));
                        signs.add(new Sign(null, "accurata", "Attesa"));
                        signs.add(new Sign(null, "minuta", "Attesa"));
                        signs.add(new Sign(null, "minuziosa", "Attesa"));
                        signs.add(new Sign(null, "pedante", "Attesa"));
                        signs.add(new Sign(null, "uguale", "Attesa"));
                        signs.add(new Sign(null, "parca", "Attesa"));
                        signs.add(new Sign(null, "staccata", "Attesa"));
                        signs.add(new Sign(null, "levigata", "Attesa"));
                        signs.add(new Sign(null, "angoliC", "Attesa"));
                        signs.add(new Sign(null, "largaDiLettere", "Attesa"));
                        signs.add(new Sign(null, "largaTraParole", "Attesa"));
                        signs.add(new Sign(null, "disugualeMetodicamente", "Attesa"));

                        signRepository.saveAll(signs);
                        System.out.println("SignDatabase initialized with default Signs.");
                } else {
                        System.out.println("SignDatabase already initialized.");
                }

                populateDatabaseFromJson(
                                new String(Files.readAllBytes(Paths.get(
                                                "src/main/resources/defaultData.json")),
                                                StandardCharsets.UTF_8));
        }

}
