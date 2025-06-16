package com.unipd.synclab.grafosupporter.utility;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.unipd.synclab.grafosupporter.model.Book;
import com.unipd.synclab.grafosupporter.model.Sign;
import com.unipd.synclab.grafosupporter.model.Combination;
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
import java.io.InputStream;
import org.springframework.core.io.ClassPathResource;

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
                ClassPathResource resource = new ClassPathResource("defaultData.json");
                ObjectMapper objectMapper = new ObjectMapper();
                try (InputStream inputStream = resource.getInputStream()) {
                        JsonRoot root = objectMapper.readValue(inputStream, JsonRoot.class);
                        List<Combination> combinations = new ArrayList<>();

                        for (JsonCombination jc : root.getCombinazioni()) {
                                Combination combination = new Combination();
                                combination.setTitle(jc.getTitle());
                                combination.setShortDescription(jc.getDescription_short());
                                combination.setLongDescription(jc.getDescription_long());
                                combination.setOriginalTextCondition(jc.getOriginal_text_condition());
                                combination.setAuthor(jc.getAuthor());
                                combination.setImagePath(jc.getImagePath());

                                List<ValuatedSign> valuatedSigns = new ArrayList<>();
                                for (JsonSign js : jc.getSigns()) {
                                        Optional<Sign> optionalSign = signRepository.findById(js.getId());
                                        if (optionalSign.isPresent()) {
                                                Sign foundSign = optionalSign.get();
                                                ValuatedSign vs = new ValuatedSign();
                                                vs.setSign(foundSign);
                                                vs.setCombination(combination);
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
                                combination.setSigns(valuatedSigns);

                                if (jc.getSource() != null) {
                                        com.unipd.synclab.grafosupporter.model.Book book = processBook(jc.getSource(),
                                                        jc.getTitle());
                                        combination.setSourceBook(book);
                                }

                                combinations.add(combination);
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
                        signs.add(new Sign(null, "Largo tra lettere", null));
                        // temperamento Cessione
                        signs.add(new Sign(null, "Curva", "Cessione"));
                        signs.add(new Sign(null, "Discendente", "Cessione"));
                        signs.add(new Sign(null, "Titubante", "Cessione"));
                        signs.add(new Sign(null, "Apertura a capo O,A", "Cessione"));
                        signs.add(new Sign(null, "Aste concave a destra", "Cessione"));
                        signs.add(new Sign(null, "Pendente", "Cessione"));
                        signs.add(new Sign(null, "Profusa", "Cessione"));
                        signs.add(new Sign(null, "Fluida", "Cessione"));
                        signs.add(new Sign(null, "Attaccata", "Cessione"));
                        // temperamento Resistenza
                        signs.add(new Sign(null, "Angoli B", "Resistenza"));
                        signs.add(new Sign(null, "Mantiene il Rigo", "Resistenza"));
                        signs.add(new Sign(null, "Secca", "Resistenza"));
                        signs.add(new Sign(null, "Aste Rette", "Resistenza"));
                        signs.add(new Sign(null, "Dritta", "Resistenza"));
                        signs.add(new Sign(null, "Recisa", "Resistenza"));
                        signs.add(new Sign(null, "Austera", "Resistenza"));
                        signs.add(new Sign(null, "Chiara", "Resistenza"));
                        // temperamento Assalto
                        signs.add(new Sign(null, "Angoli A", "Assalto"));
                        signs.add(new Sign(null, "Intozzata I Modo", "Assalto"));
                        signs.add(new Sign(null, "Ascendente", "Assalto"));
                        signs.add(new Sign(null, "Scattante", "Assalto"));
                        signs.add(new Sign(null, "Aste concave a sinistra", "Assalto"));
                        signs.add(new Sign(null, "Ardita", "Assalto"));
                        signs.add(new Sign(null, "Slanciata", "Assalto"));
                        signs.add(new Sign(null, "Impaziente", "Assalto"));
                        signs.add(new Sign(null, "Spavalda", "Assalto"));
                        signs.add(new Sign(null, "Acuta", "Assalto"));
                        signs.add(new Sign(null, "Veloce", "Assalto"));
                        signs.add(new Sign(null, "Solenne", "Assalto"));
                        // temperamento Attesa
                        signs.add(new Sign(null, "Intozzata II Modo", "Attesa"));
                        signs.add(new Sign(null, "Contorta", "Attesa"));
                        signs.add(new Sign(null, "Sinuosa", "Attesa"));
                        signs.add(new Sign(null, "Stentata", "Attesa"));
                        signs.add(new Sign(null, "Tentennante", "Attesa"));
                        signs.add(new Sign(null, "Ponderata", "Attesa"));
                        signs.add(new Sign(null, "Calma", "Attesa"));
                        signs.add(new Sign(null, "Filiforme", "Attesa"));
                        signs.add(new Sign(null, "Fine", "Attesa"));
                        signs.add(new Sign(null, "Grossa", "Attesa"));
                        signs.add(new Sign(null, "Grossolana", "Attesa"));
                        signs.add(new Sign(null, "Ricci Nascondimento", "Attesa"));
                        signs.add(new Sign(null, "Ricci Ammanieramento", "Attesa"));
                        signs.add(new Sign(null, "Ricci Mitomania", "Attesa"));
                        signs.add(new Sign(null, "Vezzosa", "Attesa"));
                        signs.add(new Sign(null, "Accurata", "Attesa"));
                        signs.add(new Sign(null, "Minuta", "Attesa"));
                        signs.add(new Sign(null, "Minuziosa", "Attesa"));
                        signs.add(new Sign(null, "Pedante", "Attesa"));
                        signs.add(new Sign(null, "Uguale", "Attesa"));
                        signs.add(new Sign(null, "Parca", "Attesa"));
                        signs.add(new Sign(null, "Staccata", "Attesa"));
                        signs.add(new Sign(null, "Levigata", "Attesa"));
                        signs.add(new Sign(null, "Angoli C", "Attesa"));
                        signs.add(new Sign(null, "Largo di lettere", "Attesa"));
                        signs.add(new Sign(null, "Largo tra parole", "Attesa"));
                        signs.add(new Sign(null, "Disuguale Metodico", "Attesa"));

                        // altri segni
                        // temp cessione
                        signs.add(new Sign(null, "Flessuosa", "Cessione"));
                        signs.add(new Sign(null, "Sciatta", "Cessione"));
                        signs.add(new Sign(null, "Flaccida", "Cessione"));
                        signs.add(new Sign(null, "Rilasciata", "Cessione"));
                        signs.add(new Sign(null, "Ricci Flemma", "Cessione"));
                        signs.add(new Sign(null, "Dilatata", "Cessione"));
                        signs.add(new Sign(null, "Frammentata", "Cessione"));
                        signs.add(new Sign(null, "Disordinata", "Cessione"));

                        // temp resistenza
                        signs.add(new Sign(null, "Lettere Addossate", "Resistenza"));
                        signs.add(new Sign(null, "Legata", "Resistenza"));
                        signs.add(new Sign(null, "Piantata sul rigo", "Resistenza"));
                        signs.add(new Sign(null, "Allungata o Alta", "Resistenza"));
                        signs.add(new Sign(null, "Stretto di lettere", "Resistenza"));
                        signs.add(new Sign(null, "Stretto tra lettere", "Resistenza"));
                        signs.add(new Sign(null, "Nitida", "Resistenza"));
                        signs.add(new Sign(null, "Aggrovigliata", "Resistenza"));
                        signs.add(new Sign(null, "Accartocciata", "Resistenza"));

                        // temp assalto
                        signs.add(new Sign(null, "Artritica", "Assalto"));
                        signs.add(new Sign(null, "Irta", "Assalto"));
                        signs.add(new Sign(null, "Ampollosa", "Assalto"));
                        signs.add(new Sign(null, "Dinamica", "Assalto"));
                        signs.add(new Sign(null, "Ricci Spavalderia", "Assalto"));
                        // temp attesa
                        signs.add(new Sign(null, "Parallela", "Attesa"));
                        signs.add(new Sign(null, "Calibro Piccolo", "Attesa"));
                        signs.add(new Sign(null, "Oscura", "Attesa"));
                        signs.add(new Sign(null, "Lenta", "Attesa"));
                        signs.add(new Sign(null, "Elegante", "Attesa"));
                        signs.add(new Sign(null, "Studiata", "Attesa"));
                        signs.add(new Sign(null, "Ricci Sobrietà", "Attesa"));
                        signs.add(new Sign(null, "Ricci Soggettivismo", "Attesa"));
                        signs.add(new Sign(null, "Ricci Stentatezza", "Attesa"));
                        signs.add(new Sign(null, "Ricci Vezzosità", "Attesa"));

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
