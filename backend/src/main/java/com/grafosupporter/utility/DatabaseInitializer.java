package com.grafosupporter.utility;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

import java.util.List;
import java.util.Optional;

import lombok.Data;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.grafosupporter.model.Book;
import com.grafosupporter.model.Combination;
import com.grafosupporter.model.Sign;
import com.grafosupporter.model.User;
import com.grafosupporter.model.ValuatedSign;
import com.grafosupporter.repository.BookRepository;
import com.grafosupporter.repository.CombinationRepository;
import com.grafosupporter.repository.SignRepository;
import com.grafosupporter.repository.UserRepository;

import java.io.IOException;
import java.io.InputStream;
import org.springframework.core.io.ClassPathResource;

import com.grafosupporter.config.ConfigurationPropertiesManager;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

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
        private final BookRepository bookRepository;
        private final SignRepository signRepository;
        private final CombinationRepository combinationRepository;
        private final UserRepository userRepository;
        private final ConfigurationPropertiesManager configProperties;

        private Long defaultUser1Id;
        private Long defaultUser2Id;

        public DatabaseInitializer(BookRepository bookRepository, SignRepository signRepository,
                        CombinationRepository combinationRepository, UserRepository userRepository,
                        ConfigurationPropertiesManager configProperties) {
                this.bookRepository = bookRepository;
                this.signRepository = signRepository;
                this.combinationRepository = combinationRepository;
                this.userRepository = userRepository;
                this.configProperties = configProperties;
        }

        private User createOrGetDefaultUser(String email, String name, String pictureUrl, String googleId) {
                return userRepository.findByGoogleId(googleId)
                                .orElseGet(() -> {
                                        User newUser = new User(email, name, pictureUrl, googleId);
                                        return userRepository.save(newUser);
                                });
        }

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

        private void initDefaultUsers() {
                User user1 = userRepository.findByEmail("girolamo.moretti@grafosupporter.local")
                                .orElseGet(() -> createOrGetDefaultUser(
                                                "girolamo.moretti@grafosupporter.local",
                                                "Girolamo Moretti",
                                                null,
                                                "default_google_id_moretti"));
                User user2 = userRepository.findByEmail("utente.default@grafosupporter.local")
                                .orElseGet(() -> createOrGetDefaultUser(
                                                "utente.default@grafosupporter.local",
                                                "Utente Default",
                                                null,
                                                "default_google_id_user"));

                defaultUser1Id = user1.getId();
                defaultUser2Id = user2.getId();
        }

        private User determineDefaultAuthor(String name) {
                if (name == null || name.trim().isEmpty()) {
                        return userRepository.findById(defaultUser2Id)
                                        .orElseThrow(() -> new RuntimeException("Utente default 2 non trovato"));
                }

                if ("Girolamo Moretti".equalsIgnoreCase(name.trim())) {
                        return userRepository.findById(defaultUser1Id)
                                        .orElseThrow(() -> new RuntimeException("Utente default 1 non trovato"));
                }

                return userRepository.findById(defaultUser2Id)
                                .orElseThrow(() -> new RuntimeException("Utente default 2 non trovato"));
        }

        public void populateDatabaseFromJson() {
                if (combinationRepository.count() > 0) {
                        System.out.println("CombinationDatabase gia pieno.");
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

                                User author = determineDefaultAuthor(jc.getAuthor());
                                combination.setAuthor(author);
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
                                        com.grafosupporter.model.Book book = processBook(jc.getSource(),
                                                        jc.getTitle());
                                        combination.setSourceBook(book);
                                }

                                combinations.add(combination);
                        }

                        combinationRepository.saveAll(combinations);

                } catch (IOException e) {
                        System.err.println("Errore nella lettura/parsing del file JSON: " + e.getMessage());
                        throw new RuntimeException("fallito il popolamento del database dal file defaultData.json", e);
                }
        }

        @EventListener(ApplicationReadyEvent.class)
        public void initBookDatabase() {
                initDefaultImages();
                initDefaultUsers();

                if (signRepository.count() == 0) {
                        final String CESSIONE = "Cessione";
                        final String RESISTENZA = "Resistenza";
                        final String ASSALTO = "Assalto";
                        final String ATTESA = "Attesa";

                        final String LARGHEZZE = "Larghezze";
                        final String CURVA_ANGOLOSA = "Curvilineità / Angolosità";
                        final String RIGO_DI_BASE = "Rigo di base";
                        final String INCLINAZIONE_DIREZIONE_ASSIALE = "Inclinazione e direzione assiale";
                        final String ASTE = "Aste letterali";
                        final String RAPIDITA = "Rapidità";
                        final String COLLEGAMENTI = "Collegamenti";
                        final String FORMA_ACCURATEZZA = "Forma e Accuratezza";
                        final String CHIAREZZA_CONFUSIONE = "Chiarezza e Confusione";
                        final String CALIBRO = "Calibro";
                        final String PRESSIONE = "Pressione";
                        final String RICCI = "Ricci";

                        ArrayList<Sign> signs = new ArrayList<>();
                        signs.add(new Sign(null, "Largo tra lettere", null, LARGHEZZE));
                        // temperamento Cessione
                        signs.add(new Sign(null, "Curva", CESSIONE, CURVA_ANGOLOSA));
                        signs.add(new Sign(null, "Discendente", CESSIONE, RIGO_DI_BASE));
                        signs.add(new Sign(null, "Titubante", CESSIONE, INCLINAZIONE_DIREZIONE_ASSIALE));
                        signs.add(new Sign(null, "Apertura a capo O,A", CESSIONE, CURVA_ANGOLOSA));
                        signs.add(new Sign(null, "Aste concave a destra", CESSIONE, ASTE));
                        signs.add(new Sign(null, "Pendente", CESSIONE, INCLINAZIONE_DIREZIONE_ASSIALE));
                        signs.add(new Sign(null, "Profusa", CESSIONE, LARGHEZZE));
                        signs.add(new Sign(null, "Fluida", CESSIONE, RAPIDITA));
                        signs.add(new Sign(null, "Attaccata", CESSIONE, COLLEGAMENTI));
                        // temperamento Resistenza
                        signs.add(new Sign(null, "Angoli B", RESISTENZA, CURVA_ANGOLOSA));
                        signs.add(new Sign(null, "Mantiene il Rigo", RESISTENZA, RIGO_DI_BASE));
                        signs.add(new Sign(null, "Secca", RESISTENZA, CURVA_ANGOLOSA));
                        signs.add(new Sign(null, "Aste Rette", RESISTENZA, ASTE));
                        signs.add(new Sign(null, "Dritta", RESISTENZA, INCLINAZIONE_DIREZIONE_ASSIALE));
                        signs.add(new Sign(null, "Recisa", RESISTENZA, FORMA_ACCURATEZZA));
                        signs.add(new Sign(null, "Austera", RESISTENZA, FORMA_ACCURATEZZA));
                        signs.add(new Sign(null, "Chiara", RESISTENZA, CHIAREZZA_CONFUSIONE));
                        // temperamento Assalto
                        signs.add(new Sign(null, "Angoli A", ASSALTO, CURVA_ANGOLOSA));

                        signs.add(new Sign(null, "Ascendente", ASSALTO, RIGO_DI_BASE));
                        signs.add(new Sign(null, "Scattante", ASSALTO, RIGO_DI_BASE));
                        signs.add(new Sign(null, "Aste concave a sinistra", ASSALTO, ASTE));
                        signs.add(new Sign(null, "Ardita", ASSALTO, RAPIDITA));
                        signs.add(new Sign(null, "Slanciata", ASSALTO, RAPIDITA));
                        signs.add(new Sign(null, "Impaziente", ASSALTO, RAPIDITA));
                        signs.add(new Sign(null, "Spavalda", ASSALTO, CALIBRO));
                        signs.add(new Sign(null, "Acuta", ASSALTO, CURVA_ANGOLOSA));
                        signs.add(new Sign(null, "Veloce", ASSALTO, RAPIDITA));
                        signs.add(new Sign(null, "Solenne", ASSALTO, CALIBRO));
                        // temperamento Attesa
                        signs.add(new Sign(null, "Intozzata II Modo", ATTESA, PRESSIONE));
                        signs.add(new Sign(null, "Contorta", ATTESA, INCLINAZIONE_DIREZIONE_ASSIALE));
                        signs.add(new Sign(null, "Sinuosa", ATTESA, INCLINAZIONE_DIREZIONE_ASSIALE));
                        signs.add(new Sign(null, "Stentata", ATTESA, INCLINAZIONE_DIREZIONE_ASSIALE));
                        signs.add(new Sign(null, "Tentennante", ATTESA, INCLINAZIONE_DIREZIONE_ASSIALE));
                        signs.add(new Sign(null, "Ponderata", ATTESA, LARGHEZZE));
                        signs.add(new Sign(null, "Calma", ATTESA, RAPIDITA));
                        signs.add(new Sign(null, "Filiforme", ATTESA, PRESSIONE));
                        signs.add(new Sign(null, "Fine", ATTESA, PRESSIONE));
                        signs.add(new Sign(null, "Grossa", ATTESA, PRESSIONE));
                        signs.add(new Sign(null, "Grossolana", ATTESA, PRESSIONE));
                        signs.add(new Sign(null, "Ricci Nascondimento", ATTESA, RICCI));
                        signs.add(new Sign(null, "Ricci Ammanieramento", ATTESA, RICCI));
                        signs.add(new Sign(null, "Ricci Mitomania", ATTESA, RICCI));
                        signs.add(new Sign(null, "Vezzosa", ATTESA, FORMA_ACCURATEZZA));
                        signs.add(new Sign(null, "Accurata", ATTESA, FORMA_ACCURATEZZA));
                        signs.add(new Sign(null, "Minuta", ATTESA, CALIBRO));
                        signs.add(new Sign(null, "Minuziosa", ATTESA, CALIBRO));
                        signs.add(new Sign(null, "Pedante", ATTESA, PRESSIONE));
                        signs.add(new Sign(null, "Uguale", ATTESA, FORMA_ACCURATEZZA));
                        signs.add(new Sign(null, "Parca", ATTESA, FORMA_ACCURATEZZA));
                        signs.add(new Sign(null, "Staccata", ATTESA, COLLEGAMENTI));
                        signs.add(new Sign(null, "Levigata", ATTESA, FORMA_ACCURATEZZA));
                        signs.add(new Sign(null, "Angoli C", ATTESA, CURVA_ANGOLOSA));
                        signs.add(new Sign(null, "Largo di lettere", ATTESA, LARGHEZZE));
                        signs.add(new Sign(null, "Largo tra parole", ATTESA, LARGHEZZE));
                        signs.add(new Sign(null, "Disuguale Metodico", ATTESA, CALIBRO));

                        // altri segni
                        // temp cessione
                        signs.add(new Sign(null, "Flessuosa", CESSIONE, FORMA_ACCURATEZZA));
                        signs.add(new Sign(null, "Sciatta", CESSIONE, FORMA_ACCURATEZZA));
                        signs.add(new Sign(null, "Flaccida", CESSIONE, FORMA_ACCURATEZZA));
                        signs.add(new Sign(null, "Rilasciata", CESSIONE, FORMA_ACCURATEZZA));
                        signs.add(new Sign(null, "Ricci Flemma", CESSIONE, RICCI));
                        signs.add(new Sign(null, "Dilatata", CESSIONE, LARGHEZZE));
                        signs.add(new Sign(null, "Frammentata", CESSIONE, COLLEGAMENTI));
                        signs.add(new Sign(null, "Disordinata", CESSIONE, CALIBRO));

                        // temp resistenza
                        signs.add(new Sign(null, "Lettere Addossate", RESISTENZA, LARGHEZZE));
                        signs.add(new Sign(null, "Legata", RESISTENZA, COLLEGAMENTI));
                        signs.add(new Sign(null, "Piantata sul rigo", RESISTENZA, RIGO_DI_BASE));
                        signs.add(new Sign(null, "Allungata o Alta", RESISTENZA, CALIBRO));
                        signs.add(new Sign(null, "Stretto di lettere", RESISTENZA, LARGHEZZE));
                        signs.add(new Sign(null, "Stretto tra lettere", RESISTENZA, LARGHEZZE));
                        signs.add(new Sign(null, "Nitida", RESISTENZA, CHIAREZZA_CONFUSIONE));
                        signs.add(new Sign(null, "Aggrovigliata", RESISTENZA, CHIAREZZA_CONFUSIONE));
                        signs.add(new Sign(null, "Accartocciata", RESISTENZA, RAPIDITA));

                        // temp assalto
                        signs.add(new Sign(null, "Artritica", ASSALTO, PRESSIONE));
                        signs.add(new Sign(null, "Irta", ASSALTO, CURVA_ANGOLOSA));
                        signs.add(new Sign(null, "Ampollosa", ASSALTO, CALIBRO));
                        signs.add(new Sign(null, "Dinamica", ASSALTO, RAPIDITA));
                        signs.add(new Sign(null, "Ricci Spavalderia", ASSALTO, RICCI));
                        // temp attesa
                        signs.add(new Sign(null, "Parallela", ATTESA, INCLINAZIONE_DIREZIONE_ASSIALE));
                        signs.add(new Sign(null, "Calibro Piccolo", ATTESA, CALIBRO));
                        signs.add(new Sign(null, "Oscura", ATTESA, CHIAREZZA_CONFUSIONE));
                        signs.add(new Sign(null, "Lenta", ATTESA, RAPIDITA));
                        signs.add(new Sign(null, "Elegante", ATTESA, FORMA_ACCURATEZZA));
                        signs.add(new Sign(null, "Studiata", ATTESA, FORMA_ACCURATEZZA));
                        signs.add(new Sign(null, "Ricci Sobrietà", ATTESA, RICCI));
                        signs.add(new Sign(null, "Ricci Soggettivismo", ATTESA, RICCI));
                        signs.add(new Sign(null, "Ricci Stentatezza", ATTESA, RICCI));
                        signs.add(new Sign(null, "Ricci Vezzosità", ATTESA, RICCI));

                        signRepository.saveAll(signs);
                        System.out.println("SignDatabase inizializzato con segni default.");
                } else {
                        System.out.println("SignDatabase gia pieno.");
                }
                if (combinationRepository.count() == 0) {
                        populateDatabaseFromJson();
                        System.out.println("CombinationDatabase inizializzato (nessuna combinazione di default).");
                } else {
                        System.out.println("CombinationDatabase gia pieno.");
                }
        }

        public void initDefaultImages() {
                System.out.println("Inizializzazione delle immagini di default...");
                try {
                        Path uploadDir = Path.of(configProperties.getCombinationImagesDirectory());
                        if (!Files.exists(uploadDir)) {
                                Files.createDirectories(uploadDir);
                        }

                        List<String> defaultImages = List.of("scrittura.jpg", "scrittura2.jpg");

                        for (String imageName : defaultImages) {
                                Path targetPath = uploadDir.resolve(imageName);

                                if (!Files.exists(targetPath)) {
                                        ClassPathResource resource = new ClassPathResource(
                                                        "static_images/" + imageName);
                                        if (resource.exists()) {
                                                try (InputStream inputStream = resource.getInputStream()) {
                                                        Files.copy(inputStream, targetPath,
                                                                        StandardCopyOption.REPLACE_EXISTING);
                                                        System.out.println("Copiata immagine di default: " + imageName);
                                                }
                                        } else {
                                                System.err.println("Impossibile trovare l'immagine nelle risorse: "
                                                                + imageName);
                                        }
                                }
                        }
                } catch (IOException e) {
                        System.err.println("Errore durante l'inizializzazione delle immagini di default: "
                                        + e.getMessage());
                        e.printStackTrace();
                }
        }

}
