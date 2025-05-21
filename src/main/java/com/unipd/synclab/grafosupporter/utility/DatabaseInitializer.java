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
import java.util.HashMap;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

@Component
public class DatabaseInitializer {
    @Autowired
    BookRepository bookRepository;
    @Autowired
    SignRepository signRepository;
    @Autowired
    SignCombinationRepository signCombinationRepository;

    // public DatabaseInitializer(BookRepository bookRepository, SignRepository
    // signRepository,
    // SignCombinationRepository signCombinationRepository) {
    // this.bookRepository = bookRepository;
    // this.signRepository = signRepository;
    // this.signCombinationRepository = signCombinationRepository;
    // }

    @EventListener(ApplicationReadyEvent.class)
    public void initBookDatabase() {
        if (bookRepository.count() == 0) {
            ArrayList<Book> books = new ArrayList<Book>();
            books.add(new Book(null, "Manuale di grafologia", "Girolamo Moretti", 1914, "Edizioni Messaggero Padova",
                    "9788825010008"));
            books.add(new Book(null, "Vizio. Psicologia e grafologia dei sette vizi capitali", "Girolamo Moretti", 1937,
                    "Edizioni Messaggero Padova", "9788825010015"));
            books.add(new Book(null, "Trattato scientifico di perizie grafiche su base grafologica", "Girolamo Moretti",
                    1942, "Edizioni Messaggero Padova", "9788825010251"));
            books.add(new Book(null, "Grafologia somatica. Il corpo umano dalla scrittura", "Girolamo Moretti", 1945,
                    "Edizioni Messaggero Padova", "9788825010022"));
            books.add(new Book(null, "Grafologia pedagogica", "Girolamo Moretti", 1947, "Edizioni Messaggero Padova",
                    "9788825010268"));
            books.add(new Book(null, "Grafologia delle attitudini umane", "Girolamo Moretti", 1948,
                    "Edizioni Messaggero Padova", "9788825010039"));
            books.add(new Book(null, "I santi dalla scrittura", "Girolamo Moretti", 1952, "Edizioni San Paolo",
                    "9788821535727"));
            books.add(new Book(null, "Trattato di grafologia. Intelligenza, sentimento", "Girolamo Moretti", 1955,
                    "Edizioni Messaggero Padova", "9788825010800"));
            books.add(new Book(null, "Scompensi, anomalie della psiche e grafologia", "Girolamo Moretti", 1962,
                    "Edizioni Messaggero Padova", "9788825010046"));
            books.add(new Book(null, "La passione predominante. Grafologia differenziale", "Girolamo Moretti", 1962,
                    "Edizioni Messaggero Padova", "9788825010053"));
            books.add(new Book(null, "Analisi grafologiche", "Girolamo Moretti", 1966, "Edizioni Messaggero Padova",
                    "9788825010060"));
            books.add(new Book(null, "I grandi dalla scrittura", "Girolamo Moretti", 1966, "Edizioni Messaggero Padova",
                    "9788825010077"));
            books.add(new Book(null, "Grafologia e pedagogia nella scuola dell'obbligo", "Girolamo Moretti", 1970,
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
            // temperamento Cessione
            signs.add(new Sign(null, "Curva"));
            signs.add(new Sign(null, "Larga fra lettere"));
            signs.add(new Sign(null, "Discendente"));
            signs.add(new Sign(null, "Titubante"));
            signs.add(new Sign(null, "Apertura a capo"));
            signs.add(new Sign(null, "Aste in avanti"));
            signs.add(new Sign(null, "Pendente"));
            signs.add(new Sign(null, "Profusa"));
            signs.add(new Sign(null, "Fluida"));
            signs.add(new Sign(null, "Attaccata"));
            signs.add(new Sign(null, "Sciatta"));
            signs.add(new Sign(null, "Flessuosa"));
            // temperamento Resistenza
            signs.add(new Sign(null, "Angoli B"));
            signs.add(new Sign(null, "Mantiene il rigo"));
            signs.add(new Sign(null, "Secca"));
            signs.add(new Sign(null, "Aste rette"));
            signs.add(new Sign(null, "Dritta"));
            signs.add(new Sign(null, "Recisa"));
            signs.add(new Sign(null, "Austera"));
            signs.add(new Sign(null, "Chiara"));
            signs.add(new Sign(null, "Nitida"));
            signs.add(new Sign(null, "Aggrovigliata"));
            signs.add(new Sign(null, "Lettere addossate"));
            signs.add(new Sign(null, "Accartocciata"));
            signs.add(new Sign(null, "Legata"));
            // temperamento Assalto
            signs.add(new Sign(null, "Angoli A"));
            signs.add(new Sign(null, "Intozzata I modo"));
            signs.add(new Sign(null, "Ascendente"));
            signs.add(new Sign(null, "Scattante"));
            signs.add(new Sign(null, "Aste indietro"));
            signs.add(new Sign(null, "Ardita"));
            signs.add(new Sign(null, "Slanciata"));
            signs.add(new Sign(null, "Impaziente"));
            signs.add(new Sign(null, "Spavalda"));
            signs.add(new Sign(null, "Acuta"));
            signs.add(new Sign(null, "Veloce"));
            signs.add(new Sign(null, "Solenne"));
            signs.add(new Sign(null, "Irta"));
            signs.add(new Sign(null, "Dinamica"));
            signs.add(new Sign(null, "Artritica"));
            // temperamento Attesa
            signs.add(new Sign(null, "Intozzata II modo"));
            signs.add(new Sign(null, "Contorta"));
            signs.add(new Sign(null, "Sinuosa"));
            signs.add(new Sign(null, "Stentata"));
            signs.add(new Sign(null, "Tentennante"));
            signs.add(new Sign(null, "Ponderata"));
            signs.add(new Sign(null, "Calma"));
            signs.add(new Sign(null, "Filiforme"));
            signs.add(new Sign(null, "Fine"));
            signs.add(new Sign(null, "Grossa grossolana"));
            signs.add(new Sign(null, "Ricci soggettivismo"));
            signs.add(new Sign(null, "Ricci nascondimento"));
            signs.add(new Sign(null, "Ricci mitomania"));
            signs.add(new Sign(null, "Ricci ammanieramento"));
            signs.add(new Sign(null, "Vezzosa grazia"));
            signs.add(new Sign(null, "Vezzosa civetteria"));
            signs.add(new Sign(null, "Accurata"));
            signs.add(new Sign(null, "Minuta"));
            signs.add(new Sign(null, "Minuziosa"));
            signs.add(new Sign(null, "Pedante"));
            signs.add(new Sign(null, "Parca"));
            signs.add(new Sign(null, "Staccata"));
            signs.add(new Sign(null, "Levigata"));
            signs.add(new Sign(null, "Angoli C"));
            signs.add(new Sign(null, "Larga di Lettere"));
            signs.add(new Sign(null, "Diseguale metodicamente"));
            signs.add(new Sign(null, "Lenta"));
            signs.add(new Sign(null, "Elegante"));
            signs.add(new Sign(null, "Parallela"));
            signs.add(new Sign(null, "Piantata sul rigo"));

            signRepository.saveAll(signs);
            System.out.println("SignDatabase initialized with default Books.");
        } else {
            System.out.println("SignDatabase already initialized.");
        }

        if (signCombinationRepository.count() == 0) {
            ArrayList<SignCombination> combinations = new ArrayList<SignCombination>();

            // // inserimento segno orgoglio
            // String combinationTitle = "Orgoglio";
            ArrayList<ValuatedSign> signsForDb = new ArrayList<ValuatedSign>();

            HashMap<String, Integer> signsToProcess = new HashMap<String, Integer>();
            // signsToProcess.put("Curva", 7);
            // signsToProcess.put("Intozzata I modo", 7); //aggiungi altri segni qui se sono
            // più di 2 nella combinazione

            // signsToProcess.entrySet().stream().forEach( sign -> {
            // String signName = sign.getKey();
            // Integer value = sign.getValue();

            // Optional<Sign> optionalSign = signRepository.findByName(signName);
            // if(optionalSign.isPresent()){
            // Sign foundSign = optionalSign.get();
            // Long signId = foundSign.getId();
            // signsForDb.add(new ValuatedSign(signId, value));
            // }else{
            // System.err.println("ATTENZIONE: Segno con nome '" + signName + "' non trovato
            // nel database. Impossibile creare ValuatedSign per la combinazione" +
            // combinationTitle);
            // }
            // });
            // combinations.add(new SignCombination(null, "","","",signsForDb,1));
            // // fine segno orgoglio

            // 1. Orgoglio (esempio fornito dall'utente, leggermente modificato)
            // 1. Orgoglio
            String combinationTitle = "Orgoglio";
            signsForDb = new ArrayList<>();
            signsToProcess = new HashMap<>();
            signsToProcess.put("Intozzata I modo", 8);
            signsToProcess.put("Mantiene il rigo", 7);
            signsToProcess.put("Slanciata", 6);
            signsForDb = processSigns(combinationTitle, signsForDb, signsToProcess);
            Book book1 = processBook((long) 8, combinationTitle);
            combinations.add(new SignCombination(null, combinationTitle, "Forte autostima, tendenza a imporsi",
                    "Combinazione di autoaffermazione e tenuta.", signsForDb, book1));

            // 2. Diplomazia
            combinationTitle = "Diplomazia";
            signsForDb = new ArrayList<>();
            signsToProcess = new HashMap<>();
            signsToProcess.put("Sinuosa", 8);
            signsToProcess.put("Ponderata", 7);
            signsToProcess.put("Ricci ammanieramento", 6); // Tocco di abilità nel presentarsi
            signsToProcess.put("Larga fra lettere", 5); // Apertura calcolata
            signsForDb = processSigns(combinationTitle, signsForDb, signsToProcess);
            Book book2 = processBook((long) 8, combinationTitle);
            combinations.add(new SignCombination(null, combinationTitle, "Abilità nel trattare con gli altri, tatto",
                    "Capacità di gestire situazioni complesse con astuzia e adattabilità.", signsForDb, book2));

            // 3. Tenacia
            combinationTitle = "Tenacia";
            signsForDb = new ArrayList<>();
            signsToProcess = new HashMap<>();
            signsToProcess.put("Mantiene il rigo", 9);
            signsToProcess.put("Intozzata I modo", 7);
            signsToProcess.put("Aste rette", 7);
            signsToProcess.put("Legata", 6); // Continuità nello sforzo
            signsForDb = processSigns(combinationTitle, signsForDb, signsToProcess);
            Book book3 = processBook((long) 1, combinationTitle);
            combinations.add(new SignCombination(null, combinationTitle, "Perseveranza, costanza negli obiettivi",
                    "Forte determinazione e resistenza alla fatica e agli ostacoli.", signsForDb, book3));

            // 4. Generosità Impulsiva
            combinationTitle = "Generosità Impulsiva";
            signsForDb = new ArrayList<>();
            signsToProcess = new HashMap<>();
            signsToProcess.put("Profusa", 8);
            signsToProcess.put("Larga fra lettere", 7);
            signsToProcess.put("Slanciata", 7);
            signsToProcess.put("Veloce", 6);
            signsForDb = processSigns(combinationTitle, signsForDb, signsToProcess);
            Book book4 = processBook((long) 6, combinationTitle);
            combinations.add(new SignCombination(null, combinationTitle, "Disponibilità immediata, slancio altruistico",
                    "Tendenza a dare senza troppa riflessione, con calore.", signsForDb, book4));

            // 5. Introversione Riflessiva
            combinationTitle = "Introversione Riflessiva";
            signsForDb = new ArrayList<>();
            signsToProcess = new HashMap<>();
            signsToProcess.put("Parca", 8);
            signsToProcess.put("Staccata", 7);
            signsToProcess.put("Ponderata", 7);
            signsToProcess.put("Minuta", 6);
            signsForDb = processSigns(combinationTitle, signsForDb, signsToProcess);
            Book book5 = processBook((long) 8, combinationTitle);
            combinations.add(
                    new SignCombination(null, combinationTitle, "Tendenza all'interiorizzazione e alla riflessione",
                            "Preferenza per la contemplazione e l'analisi interiore.", signsForDb, book5));

            // 6. Ambizione Determinata
            combinationTitle = "Ambizione Determinata";
            signsForDb = new ArrayList<>();
            signsToProcess = new HashMap<>();
            signsToProcess.put("Ascendente", 8);
            signsToProcess.put("Intozzata I modo", 7);
            signsToProcess.put("Veloce", 6);
            signsToProcess.put("Aste rette", 7);
            signsForDb = processSigns(combinationTitle, signsForDb, signsToProcess);
            Book book6 = processBook((long) 6, combinationTitle);
            combinations.add(
                    new SignCombination(null, combinationTitle, "Forte spinta al successo, con chiarezza di obiettivi",
                            "Desiderio di realizzazione supportato da energia e decisione.", signsForDb, book6));

            // 7. Spirito Critico Acuto
            combinationTitle = "Spirito Critico Acuto";
            signsForDb = new ArrayList<>();
            signsToProcess = new HashMap<>();
            signsToProcess.put("Acuta", 8);
            signsToProcess.put("Secca", 7);
            signsToProcess.put("Parca", 6);
            signsToProcess.put("Staccata", 6); // Analisi dettagliata
            signsForDb = processSigns(combinationTitle, signsForDb, signsToProcess);
            Book book7 = processBook((long) 8, combinationTitle);
            combinations.add(
                    new SignCombination(null, combinationTitle, "Capacità di analisi penetrante, giudizio tagliente",
                            "Tendenza a esaminare a fondo e a non lasciarsi ingannare.", signsForDb, book7));

            // 8. Adattabilità Flessibile
            combinationTitle = "Adattabilità Flessibile";
            signsForDb = new ArrayList<>();
            signsToProcess = new HashMap<>();
            signsToProcess.put("Curva", 8);
            signsToProcess.put("Flessuosa", 7);
            signsToProcess.put("Larga fra lettere", 6);
            signsForDb = processSigns(combinationTitle, signsForDb, signsToProcess);
            Book book8 = processBook((long) 1, combinationTitle);
            combinations.add(new SignCombination(null, combinationTitle, "Capacità di adeguarsi a nuove situazioni",
                    "Apertura al cambiamento e facilità di interazione.", signsForDb, book8));

            // 9. Impazienza Attiva
            combinationTitle = "Impazienza Attiva";
            signsForDb = new ArrayList<>();
            signsToProcess = new HashMap<>();
            signsToProcess.put("Impaziente", 9);
            signsToProcess.put("Scattante", 8);
            signsToProcess.put("Veloce", 7);
            signsToProcess.put("Angoli A", 6);
            signsForDb = processSigns(combinationTitle, signsForDb, signsToProcess);
            Book book9 = processBook((long) 6, combinationTitle);
            combinations.add(new SignCombination(null, combinationTitle,
                    "Bisogno di agire rapidamente, poca tolleranza per l'attesa",
                    "Energia focalizzata sull'azione immediata.", signsForDb, book9));

            // 10. Prudenza Eccessiva
            combinationTitle = "Prudenza Eccessiva";
            signsForDb = new ArrayList<>();
            signsToProcess = new HashMap<>();
            signsToProcess.put("Tentennante", 8);
            signsToProcess.put("Stentata", 7);
            signsToProcess.put("Ponderata", 7);
            signsToProcess.put("Minuta", 6);
            signsForDb = processSigns(combinationTitle, signsForDb, signsToProcess);
            Book book10 = processBook((long) 9, combinationTitle);
            combinations.add(new SignCombination(null, combinationTitle, "Eccessiva cautela, timore di sbagliare",
                    "Rallentamento dell'azione dovuto a continua valutazione dei rischi.", signsForDb, book10));

            // 11. Originalità Creativa
            combinationTitle = "Originalità Creativa";
            signsForDb = new ArrayList<>();
            signsToProcess = new HashMap<>();
            signsToProcess.put("Diseguale metodicamente", 8); // Moretti la lega all'originalità
            signsToProcess.put("Slanciata", 7);
            signsToProcess.put("Apertura a capo", 6); // Apertura mentale
            signsForDb = processSigns(combinationTitle, signsForDb, signsToProcess);
            Book book11 = processBook((long) 6, combinationTitle);
            combinations.add(new SignCombination(null, combinationTitle, "Pensiero innovativo, inventiva",
                    "Capacità di trovare soluzioni nuove e non convenzionali.", signsForDb, book11));

            // 12. Disordine Pratico
            combinationTitle = "Disordine Pratico";
            signsForDb = new ArrayList<>();
            signsToProcess = new HashMap<>();
            signsToProcess.put("Sciatta", 8);
            signsToProcess.put("Profusa", 7); // Può contribuire a una certa dispersività
            signsToProcess.put("Discendente", 6); // Calo di energia o attenzione
            signsForDb = processSigns(combinationTitle, signsForDb, signsToProcess);
            Book book12 = processBook((long) 9, combinationTitle);
            combinations
                    .add(new SignCombination(null, combinationTitle, "Mancanza di organizzazione nella quotidianità",
                            "Tendenza alla trascuratezza e poca cura dei dettagli pratici.", signsForDb, book12));

            // 13. Sensibilità Vulnerabile
            combinationTitle = "Sensibilità Vulnerabile";
            signsForDb = new ArrayList<>();
            signsToProcess = new HashMap<>();
            signsToProcess.put("Fine", 8);
            signsToProcess.put("Pendente", 7);
            signsToProcess.put("Titubante", 6);
            signsForDb = processSigns(combinationTitle, signsForDb, signsToProcess);
            Book book13 = processBook((long) 8, combinationTitle);
            combinations.add(new SignCombination(null, combinationTitle, "Facilità ad essere feriti emotivamente",
                    "Grande recettività emotiva che può portare a fragilità.", signsForDb, book13));

            // 14. Precisione Metodica
            combinationTitle = "Precisione Metodica";
            signsForDb = new ArrayList<>();
            signsToProcess = new HashMap<>();
            signsToProcess.put("Accurata", 9);
            signsToProcess.put("Minuziosa", 8);
            signsToProcess.put("Parca", 7);
            signsToProcess.put("Mantiene il rigo", 7);
            signsForDb = processSigns(combinationTitle, signsForDb, signsToProcess);
            Book book14 = processBook((long) 5, combinationTitle);
            combinations
                    .add(new SignCombination(null, combinationTitle, "Grande attenzione ai dettagli, metodo rigoroso",
                            "Ricerca della perfezione e dell'ordine.", signsForDb, book14)); // Grafologia Pedagogica

            // 15. Agitazione Interiore
            combinationTitle = "Agitazione Interiore";
            signsForDb = new ArrayList<>();
            signsToProcess = new HashMap<>();
            signsToProcess.put("Irta", 7);
            signsToProcess.put("Scattante", 7);
            signsToProcess.put("Contorta", 6); // Tensione interna
            signsToProcess.put("Impaziente", 7);
            signsForDb = processSigns(combinationTitle, signsForDb, signsToProcess);
            Book book15 = processBook((long) 9, combinationTitle);
            combinations.add(new SignCombination(null, combinationTitle, "Stato di nervosismo e irrequietezza",
                    "Difficoltà a trovare calma e stabilità emotiva.", signsForDb, book15));

            // 16. Idealismo Appassionato
            combinationTitle = "Idealismo Appassionato";
            signsForDb = new ArrayList<>();
            signsToProcess = new HashMap<>();
            signsToProcess.put("Slanciata", 8);
            signsToProcess.put("Ascendente", 7);
            signsToProcess.put("Veloce", 7);
            signsToProcess.put("Apertura a capo", 6); // Apertura a grandi ideali
            signsForDb = processSigns(combinationTitle, signsForDb, signsToProcess);
            Book book16 = processBook((long) 7, combinationTitle);
            combinations.add(new SignCombination(null, combinationTitle,
                    "Forte adesione a ideali elevati, con trasporto emotivo", "Spinta da nobili cause e aspirazioni.",
                    signsForDb, book16)); // I santi dalla scrittura

            // 17. Chiusura Difensiva
            combinationTitle = "Chiusura Difensiva";
            signsForDb = new ArrayList<>();
            signsToProcess = new HashMap<>();
            signsToProcess.put("Lettere addossate", 8);
            signsToProcess.put("Accartocciata", 7);
            signsToProcess.put("Ricci nascondimento", 7);
            signsToProcess.put("Parca", 6);
            signsForDb = processSigns(combinationTitle, signsForDb, signsToProcess);
            Book book17 = processBook((long) 9, combinationTitle);
            combinations.add(new SignCombination(null, combinationTitle, "Atteggiamento riservato e protettivo",
                    "Diffidenza e tendenza a celare i propri pensieri ed emozioni.", signsForDb, book17));

            // 18. Dinamismo Intraprendente
            combinationTitle = "Dinamismo Intraprendente";
            signsForDb = new ArrayList<>();
            signsToProcess = new HashMap<>();
            signsToProcess.put("Dinamica", 9);
            signsToProcess.put("Veloce", 8);
            signsToProcess.put("Angoli A", 7);
            signsToProcess.put("Larga fra lettere", 6); // Spazio per agire
            signsForDb = processSigns(combinationTitle, signsForDb, signsToProcess);
            Book book18 = processBook((long) 6, combinationTitle);
            combinations
                    .add(new SignCombination(null, combinationTitle, "Energia e iniziativa nel perseguire obiettivi",
                            "Proattività e capacità di traino.", signsForDb, book18));

            // 19. Formalismo Rigido
            combinationTitle = "Formalismo Rigido";
            signsForDb = new ArrayList<>();
            signsToProcess = new HashMap<>();
            signsToProcess.put("Austera", 8);
            signsToProcess.put("Aste rette", 8);
            signsToProcess.put("Parallela", 7); // Controllo formale
            signsToProcess.put("Pedante", 6); // Eccesso di precisione formale
            signsForDb = processSigns(combinationTitle, signsForDb, signsToProcess);
            Book book19 = processBook((long) 3, combinationTitle);
            combinations.add(new SignCombination(null, combinationTitle, "Adesione stretta alle regole e alle forme",
                    "Scarsa flessibilità e spontaneità.", signsForDb, book19)); // Trattato scientifico di perizie
                                                                                // grafiche

            // 20. Comunicativa Espansiva
            combinationTitle = "Comunicativa Espansiva";
            signsForDb = new ArrayList<>();
            signsToProcess = new HashMap<>();
            signsToProcess.put("Larga fra lettere", 8);
            signsToProcess.put("Larga di Lettere", 7); // Spazio interno alla lettera
            signsToProcess.put("Fluida", 7);
            signsToProcess.put("Curva", 6);
            signsForDb = processSigns(combinationTitle, signsForDb, signsToProcess);
            Book book20 = processBook((long) 1, combinationTitle);
            combinations.add(new SignCombination(null, combinationTitle, "Facilità di parola, apertura agli altri",
                    "Bisogno di socializzare e condividere.", signsForDb, book20));

            // 21. Acutezza Osservativa
            combinationTitle = "Acutezza Osservativa";
            signsForDb = new ArrayList<>();
            signsToProcess = new HashMap<>();
            signsToProcess.put("Acuta", 8);
            signsToProcess.put("Nitida", 7);
            signsToProcess.put("Chiara", 7);
            signsToProcess.put("Minuziosa", 6);
            signsForDb = processSigns(combinationTitle, signsForDb, signsToProcess);
            Book book21 = processBook((long) 8, combinationTitle);
            combinations.add(new SignCombination(null, combinationTitle, "Capacità di cogliere dettagli sottili",
                    "Sguardo penetrante e analitico.", signsForDb, book21));

            // 22. Volontà Debole
            combinationTitle = "Volontà Debole";
            signsForDb = new ArrayList<>();
            signsToProcess = new HashMap<>();
            signsToProcess.put("Pendente", 8);
            signsToProcess.put("Discendente", 7);
            signsToProcess.put("Titubante", 7);
            signsToProcess.put("Filiforme", 6); // Poca energia
            signsForDb = processSigns(combinationTitle, signsForDb, signsToProcess);
            Book book22 = processBook((long) 9, combinationTitle);
            combinations
                    .add(new SignCombination(null, combinationTitle, "Scarsa determinazione, facile suggestionabilità",
                            "Difficoltà a mantenere gli impegni e a resistere alle pressioni.", signsForDb, book22));

            // 23. Riservatezza Orgogliosa
            combinationTitle = "Riservatezza Orgogliosa";
            signsForDb = new ArrayList<>();
            signsToProcess = new HashMap<>();
            signsToProcess.put("Parca", 8);
            signsToProcess.put("Intozzata I modo", 7); // Autoaffermazione che può portare a chiusura
            signsToProcess.put("Mantiene il rigo", 7);
            signsToProcess.put("Staccata", 6);
            signsForDb = processSigns(combinationTitle, signsForDb, signsToProcess);
            Book book23 = processBook((long) 2, combinationTitle);
            combinations.add(new SignCombination(null, combinationTitle,
                    "Tendenza a non aprirsi, mantenendo un'alta stima di sé",
                    "Freddezza apparente dovuta a orgoglio e controllo.", signsForDb, book23)); // Vizio (Superbia)

            // 24. Irritabilità
            combinationTitle = "Irritabilità";
            signsForDb = new ArrayList<>();
            signsToProcess = new HashMap<>();
            signsToProcess.put("Irta", 8);
            signsToProcess.put("Angoli A", 7);
            signsToProcess.put("Scattante", 7);
            signsToProcess.put("Acuta", 6); // Pungente
            signsForDb = processSigns(combinationTitle, signsForDb, signsToProcess);
            Book book24 = processBook((long) 9, combinationTitle);
            combinations.add(new SignCombination(null, combinationTitle, "Facilità a innervosirsi, reattività",
                    "Suscettibilità e risposte umorali.", signsForDb, book24));

            // 25. Ostinazione
            combinationTitle = "Ostinazione";
            signsForDb = new ArrayList<>();
            signsToProcess = new HashMap<>();
            signsToProcess.put("Intozzata I modo", 8);
            signsToProcess.put("Angoli B", 7); // Resistenza passiva
            signsToProcess.put("Aste rette", 7);
            signsToProcess.put("Mantiene il rigo", 8);
            signsForDb = processSigns(combinationTitle, signsForDb, signsToProcess);
            Book book25 = processBook((long) 1, combinationTitle);
            combinations.add(new SignCombination(null, combinationTitle, "Caparbietà, difficoltà a cambiare idea",
                    "Fermezza che può sfociare in testardaggine.", signsForDb, book25));

            // 26. Superficialità
            combinationTitle = "Superficialità";
            signsForDb = new ArrayList<>();
            signsToProcess = new HashMap<>();
            signsToProcess.put("Sciatta", 7);
            signsToProcess.put("Veloce", 7); // Senza approfondire
            signsToProcess.put("Profusa", 6); // Dispersiva
            signsToProcess.put("Apertura a capo", 5); // A volte segno di non approfondimento
            signsForDb = processSigns(combinationTitle, signsForDb, signsToProcess);
            Book book26 = processBook((long) 2, combinationTitle);
            combinations.add(new SignCombination(null, combinationTitle, "Mancanza di profondità, leggerezza eccessiva",
                    "Tendenza a fermarsi all'apparenza.", signsForDb, book26));

            // 27. Seduzione Calcolata
            combinationTitle = "Seduzione Calcolata";
            signsForDb = new ArrayList<>();
            signsToProcess = new HashMap<>();
            signsToProcess.put("Vezzosa civetteria", 8);
            signsToProcess.put("Ricci ammanieramento", 7);
            signsToProcess.put("Sinuosa", 7);
            signsToProcess.put("Ponderata", 6); // Calcolo
            signsForDb = processSigns(combinationTitle, signsForDb, signsToProcess);
            Book book27 = processBook((long) 10, combinationTitle);
            combinations.add(new SignCombination(null, combinationTitle, "Abilità nel piacere e attrarre con strategia",
                    "Comportamento affascinante ma non del tutto spontaneo.", signsForDb, book27)); // La passione
                                                                                                    // predominante

            // 28. Inquietudine Creativa
            combinationTitle = "Inquietudine Creativa";
            signsForDb = new ArrayList<>();
            signsToProcess = new HashMap<>();
            signsToProcess.put("Irta", 7);
            signsToProcess.put("Diseguale metodicamente", 7);
            signsToProcess.put("Scattante", 6);
            signsToProcess.put("Ascendente", 6); // Tensione verso l'alto
            signsForDb = processSigns(combinationTitle, signsForDb, signsToProcess);
            Book book28 = processBook((long) 6, combinationTitle);
            combinations.add(new SignCombination(null, combinationTitle, "Irrequietezza che alimenta la creatività",
                    "Bisogno di esplorare e creare spinto da una tensione interna.", signsForDb, book28));

            // 29. Indecisione Ansiosa
            combinationTitle = "Indecisione Ansiosa";
            signsForDb = new ArrayList<>();
            signsToProcess = new HashMap<>();
            signsToProcess.put("Titubante", 9);
            signsToProcess.put("Tentennante", 8);
            signsToProcess.put("Stentata", 7);
            signsToProcess.put("Pendente", 6); // Peso dell'ansia
            signsForDb = processSigns(combinationTitle, signsForDb, signsToProcess);
            Book book29 = processBook((long) 9, combinationTitle);
            combinations.add(new SignCombination(null, combinationTitle, "Difficoltà a scegliere accompagnata da ansia",
                    "Blocco decisionale dovuto a timori e incertezze.", signsForDb, book29));

            // 30. Realismo Pratico
            combinationTitle = "Realismo Pratico";
            signsForDb = new ArrayList<>();
            signsToProcess = new HashMap<>();
            signsToProcess.put("Mantiene il rigo", 8);
            signsToProcess.put("Chiara", 7);
            signsToProcess.put("Nitida", 7);
            signsToProcess.put("Parca", 6); // Essenzialità
            signsForDb = processSigns(combinationTitle, signsForDb, signsToProcess);
            Book book30 = processBook((long) 1, combinationTitle);
            combinations.add(new SignCombination(null, combinationTitle, "Visione concreta della realtà, senso pratico",
                    "Piedi per terra, orientamento alla soluzione.", signsForDb, book30));

            // 31. Passionalità Controllata
            combinationTitle = "Passionalità Controllata";
            signsForDb = new ArrayList<>();
            signsToProcess = new HashMap<>();
            signsToProcess.put("Intozzata I modo", 8); // Intensità
            signsToProcess.put("Legata", 7); // Continuità emotiva
            signsToProcess.put("Mantiene il rigo", 7); // Controllo
            signsToProcess.put("Sinuosa", 6); // Abilità nel modulare
            signsForDb = processSigns(combinationTitle, signsForDb, signsToProcess);
            Book book31 = processBook((long) 10, combinationTitle);
            combinations.add(
                    new SignCombination(null, combinationTitle, "Forte intensità emotiva gestita con autocontrollo",
                            "Sentimenti profondi ma non manifestati impulsivamente.", signsForDb, book31));

            // 32. Sospettosità
            combinationTitle = "Sospettosità";
            signsForDb = new ArrayList<>();
            signsToProcess = new HashMap<>();
            signsToProcess.put("Ricci nascondimento", 8);
            signsToProcess.put("Angoli C", 7); // Diffidenza, circospezione
            signsToProcess.put("Staccata", 7);
            signsToProcess.put("Acuta", 6); // Per cogliere indizi
            signsForDb = processSigns(combinationTitle, signsForDb, signsToProcess);
            Book book32 = processBook((long) 9, combinationTitle);
            combinations.add(new SignCombination(null, combinationTitle, "Tendenza a dubitare degli altri, diffidenza",
                    "Atteggiamento guardingo e poco fiducioso.", signsForDb, book32));

            // 33. Altruismo Discreto
            combinationTitle = "Altruismo Discreto";
            signsForDb = new ArrayList<>();
            signsToProcess = new HashMap<>();
            signsToProcess.put("Curva", 7);
            signsToProcess.put("Larga fra lettere", 7);
            signsToProcess.put("Fine", 6); // Delicatezza
            signsToProcess.put("Parca", 6); // Senza ostentazione
            signsForDb = processSigns(combinationTitle, signsForDb, signsToProcess);
            Book book33 = processBook((long) 7, combinationTitle);
            combinations.add(new SignCombination(null, combinationTitle, "Generosità non ostentata, aiuto silenzioso",
                    "Disponibilità verso gli altri con umiltà e riservatezza.", signsForDb, book33));

            // 34. Pedanteria
            combinationTitle = "Pedanteria";
            signsForDb = new ArrayList<>();
            signsToProcess = new HashMap<>();
            signsToProcess.put("Pedante", 9);
            signsToProcess.put("Accurata", 8);
            signsToProcess.put("Minuziosa", 8);
            signsToProcess.put("Parca", 7);
            signsForDb = processSigns(combinationTitle, signsForDb, signsToProcess);
            Book book34 = processBook((long) 5, combinationTitle);
            combinations.add(new SignCombination(null, combinationTitle, "Eccessiva meticolosità, pignoleria",
                    "Attenzione esagerata ai dettagli formali.", signsForDb, book34));

            // 35. Leadership Autoritaria
            combinationTitle = "Leadership Autoritaria";
            signsForDb = new ArrayList<>();
            signsToProcess = new HashMap<>();
            signsToProcess.put("Intozzata I modo", 9);
            signsToProcess.put("Aste rette", 8);
            signsToProcess.put("Mantiene il rigo", 8);
            signsToProcess.put("Angoli A", 7); // Imposizione
            signsForDb = processSigns(combinationTitle, signsForDb, signsToProcess);
            Book book35 = processBook((long) 6, combinationTitle);
            combinations.add(new SignCombination(null, combinationTitle, "Tendenza a comandare in modo direttivo",
                    "Capacità di guida con forte imposizione della propria volontà.", signsForDb, book35));

            // 36. Flemma
            combinationTitle = "Flemma";
            signsForDb = new ArrayList<>();
            signsToProcess = new HashMap<>();
            signsToProcess.put("Calma", 9);
            signsToProcess.put("Lenta", 8);
            signsToProcess.put("Ponderata", 7);
            signsToProcess.put("Mantiene il rigo", 6); // Stabilità, ma può essere anche inerzia
            signsForDb = processSigns(combinationTitle, signsForDb, signsToProcess);
            Book book36 = processBook((long) 8, combinationTitle);
            combinations.add(new SignCombination(null, combinationTitle, "Estrema calma, lentezza, imperturbabilità",
                    "Scarsa reattività agli stimoli esterni.", signsForDb, book36));

            // 37. Vanità Estetica
            combinationTitle = "Vanità Estetica";
            signsForDb = new ArrayList<>();
            signsToProcess = new HashMap<>();
            signsToProcess.put("Vezzosa grazia", 8);
            signsToProcess.put("Elegante", 8);
            signsToProcess.put("Ricci ammanieramento", 7); // Cura dell'apparenza
            signsToProcess.put("Accurata", 7);
            signsForDb = processSigns(combinationTitle, signsForDb, signsToProcess);
            Book book37 = processBook((long) 2, combinationTitle);
            combinations.add(
                    new SignCombination(null, combinationTitle, "Eccessiva cura della propria immagine e apparenza",
                            "Ricerca di ammirazione per l'aspetto esteriore.", signsForDb, book37)); // Vizio

            // 38. Aggressività Latente
            combinationTitle = "Aggressività Latente";
            signsForDb = new ArrayList<>();
            signsToProcess = new HashMap<>();
            signsToProcess.put("Intozzata II modo", 8); // Pressione trattenuta che può esplodere
            signsToProcess.put("Angoli A", 7);
            signsToProcess.put("Acuta", 7);
            signsToProcess.put("Irta", 6); // Tensione
            signsForDb = processSigns(combinationTitle, signsForDb, signsToProcess);
            Book book38 = processBook((long) 9, combinationTitle);
            combinations.add(new SignCombination(null, combinationTitle,
                    "Tendenza aggressiva non manifesta ma pronta a emergere", "Impulsi ostili controllati ma presenti.",
                    signsForDb, book38));

            // 39. Abilità Manuale Precisa
            combinationTitle = "Abilità Manuale Precisa";
            signsForDb = new ArrayList<>();
            signsToProcess = new HashMap<>();
            signsToProcess.put("Nitida", 8);
            signsToProcess.put("Accurata", 8);
            signsToProcess.put("Fine", 7); // Precisione nel tratto
            signsToProcess.put("Legata", 6); // Coordinazione
            signsForDb = processSigns(combinationTitle, signsForDb, signsToProcess);
            Book book39 = processBook((long) 4, combinationTitle);
            combinations.add(new SignCombination(null, combinationTitle, "Destrezza e precisione nei lavori manuali",
                    "Buona coordinazione e attenzione ai dettagli pratici.", signsForDb, book39)); // Grafologia
                                                                                                   // somatica

            // 40. Malinconia
            combinationTitle = "Malinconia";
            signsForDb = new ArrayList<>();
            signsToProcess = new HashMap<>();
            signsToProcess.put("Discendente", 8);
            signsToProcess.put("Pendente", 8);
            signsToProcess.put("Lenta", 7);
            signsToProcess.put("Filiforme", 6); // Poca vitalità
            signsForDb = processSigns(combinationTitle, signsForDb, signsToProcess);
            Book book40 = processBook((long) 9, combinationTitle);
            combinations.add(new SignCombination(null, combinationTitle, "Stato d'animo triste e pessimista",
                    "Tendenza alla tristezza e alla riflessione nostalgica.", signsForDb, book40));

            // 41. Eloquenza Persuasiva
            combinationTitle = "Eloquenza Persuasiva";
            signsForDb = new ArrayList<>();
            signsToProcess = new HashMap<>();
            signsToProcess.put("Fluida", 8);
            signsToProcess.put("Slanciata", 7); // Impatto
            signsToProcess.put("Legata", 7); // Continuità del discorso
            signsToProcess.put("Sinuosa", 6); // Abilità dialettica
            signsForDb = processSigns(combinationTitle, signsForDb, signsToProcess);
            Book book41 = processBook((long) 6, combinationTitle);
            combinations.add(
                    new SignCombination(null, combinationTitle, "Capacità di parlare in modo fluente e convincente",
                            "Abilità nell'influenzare gli altri con le parole.", signsForDb, book41));

            // 42. Testardaggine Orgogliosa
            combinationTitle = "Testardaggine Orgogliosa";
            signsForDb = new ArrayList<>();
            signsToProcess = new HashMap<>();
            signsToProcess.put("Intozzata I modo", 9);
            signsToProcess.put("Angoli B", 8);
            signsToProcess.put("Mantiene il rigo", 8);
            signsToProcess.put("Aste rette", 7);
            signsForDb = processSigns(combinationTitle, signsForDb, signsToProcess);
            Book book42 = processBook((long) 2, combinationTitle);
            combinations.add(new SignCombination(null, combinationTitle, "Ostinazione derivante da un forte orgoglio",
                    "Difficoltà ad ammettere errori o cedere posizioni.", signsForDb, book42));

            // 43. Ricerca di Approvazione
            combinationTitle = "Ricerca di Approvazione";
            signsForDb = new ArrayList<>();
            signsToProcess = new HashMap<>();
            signsToProcess.put("Vezzosa civetteria", 7);
            signsToProcess.put("Ricci ammanieramento", 7);
            signsToProcess.put("Curva", 6); // Desiderio di piacere
            signsToProcess.put("Apertura a capo", 6); // Apertura verso l'esterno per cercare conferme
            signsForDb = processSigns(combinationTitle, signsForDb, signsToProcess);
            Book book43 = processBook((long) 10, combinationTitle);
            combinations.add(new SignCombination(null, combinationTitle, "Bisogno costante di conferme e lodi esterne",
                    "L'autostima dipende molto dal giudizio altrui.", signsForDb, book43));

            // 44. Insofferenza alle Regole
            combinationTitle = "Insofferenza alle Regole";
            signsForDb = new ArrayList<>();
            signsToProcess = new HashMap<>();
            signsToProcess.put("Spavalda", 8);
            signsToProcess.put("Ardita", 7);
            signsToProcess.put("Diseguale metodicamente", 7); // Contro le convenzioni
            signsToProcess.put("Veloce", 6); // Impulso a trasgredire
            signsForDb = processSigns(combinationTitle, signsForDb, signsToProcess);
            Book book44 = processBook((long) 6, combinationTitle);
            combinations.add(new SignCombination(null, combinationTitle, "Difficoltà ad accettare norme e costrizioni",
                    "Tendenza a sfidare l'autorità e le convenzioni.", signsForDb, book44));

            // 45. Scrupolosità Morale
            combinationTitle = "Scrupolosità Morale";
            signsForDb = new ArrayList<>();
            signsToProcess = new HashMap<>();
            signsToProcess.put("Accurata", 8);
            signsToProcess.put("Mantiene il rigo", 8); // Rettitudine
            signsToProcess.put("Parca", 7); // Essenzialità, rigore
            signsToProcess.put("Dritta", 7);
            signsForDb = processSigns(combinationTitle, signsForDb, signsToProcess);
            Book book45 = processBook((long) 7, combinationTitle);
            combinations.add(new SignCombination(null, combinationTitle, "Grande attenzione ai principi etici e morali",
                    "Senso del dovere e della giustizia molto sviluppato.", signsForDb, book45));

            // 46. Entusiasmo Contagioso
            combinationTitle = "Entusiasmo Contagioso";
            signsForDb = new ArrayList<>();
            signsToProcess = new HashMap<>();
            signsToProcess.put("Slanciata", 8);
            signsToProcess.put("Ascendente", 8);
            signsToProcess.put("Veloce", 7);
            signsToProcess.put("Profusa", 7); // Espansività
            signsForDb = processSigns(combinationTitle, signsForDb, signsToProcess);
            Book book46 = processBook((long) 6, combinationTitle);
            combinations.add(new SignCombination(null, combinationTitle, "Passione e vivacità che ispirano gli altri",
                    "Capacità di trasmettere energia positiva e coinvolgimento.", signsForDb, book46));

            // 47. Timidezza Ritirata
            combinationTitle = "Timidezza Ritirata";
            signsForDb = new ArrayList<>();
            signsToProcess = new HashMap<>();
            signsToProcess.put("Minuta", 8);
            signsToProcess.put("Pendente", 7);
            signsToProcess.put("Staccata", 7);
            signsToProcess.put("Filiforme", 6); // Poca assertività
            signsForDb = processSigns(combinationTitle, signsForDb, signsToProcess);
            Book book47 = processBook((long) 5, combinationTitle);
            combinations.add(new SignCombination(null, combinationTitle, "Riservatezza eccessiva, tendenza a isolarsi",
                    "Difficoltà nelle interazioni sociali per insicurezza.", signsForDb, book47));

            // 48. Razionalità Fredda
            combinationTitle = "Razionalità Fredda";
            signsForDb = new ArrayList<>();
            signsToProcess = new HashMap<>();
            signsToProcess.put("Secca", 8);
            signsToProcess.put("Aste rette", 8);
            signsToProcess.put("Parca", 7);
            signsToProcess.put("Staccata", 7); // Distacco emotivo
            signsForDb = processSigns(combinationTitle, signsForDb, signsToProcess);
            Book book48 = processBook((long) 3, combinationTitle);
            combinations
                    .add(new SignCombination(null, combinationTitle, "Prevalenza della logica sulle emozioni, distacco",
                            "Approccio analitico e impersonale alla realtà.", signsForDb, book48));

            // 49. Spirito Polemico
            combinationTitle = "Spirito Polemico";
            signsForDb = new ArrayList<>();
            signsToProcess = new HashMap<>();
            signsToProcess.put("Angoli A", 8);
            signsToProcess.put("Acuta", 8);
            signsToProcess.put("Irta", 7);
            signsToProcess.put("Scattante", 7);
            signsForDb = processSigns(combinationTitle, signsForDb, signsToProcess);
            Book book49 = processBook((long) 10, combinationTitle);
            combinations.add(new SignCombination(null, combinationTitle, "Tendenza a discutere e contraddire",
                    "Piacere nella disputa e nella critica.", signsForDb, book49));

            // 50. Curiosità Intellettuale
            combinationTitle = "Curiosità Intellettuale";
            signsForDb = new ArrayList<>();
            signsToProcess = new HashMap<>();
            signsToProcess.put("Apertura a capo", 8); // Apertura a nuove idee
            signsToProcess.put("Veloce", 7); // Rapidità di apprendimento
            signsToProcess.put("Chiara", 7); // Chiarezza di pensiero
            signsToProcess.put("Larga fra lettere", 6); // Disponibilità a recepire
            signsForDb = processSigns(combinationTitle, signsForDb, signsToProcess);
            Book book50 = processBook((long) 8, combinationTitle);
            combinations.add(new SignCombination(null, combinationTitle, "Desiderio di conoscere e apprendere",
                    "Interesse vivace per vari campi del sapere.", signsForDb, book50));

            // 51. Pazienza Costruttiva
            combinationTitle = "Pazienza Costruttiva";
            signsForDb = new ArrayList<>();
            signsToProcess = new HashMap<>();
            signsToProcess.put("Calma", 8);
            signsToProcess.put("Ponderata", 8);
            signsToProcess.put("Mantiene il rigo", 7); // Costanza
            signsToProcess.put("Legata", 7); // Continuità
            signsForDb = processSigns(combinationTitle, signsForDb, signsToProcess);
            Book book51 = processBook((long) 1, combinationTitle);
            combinations
                    .add(new SignCombination(null, combinationTitle, "Capacità di attendere con perseveranza e metodo",
                            "Costanza nel lavoro verso un obiettivo a lungo termine.", signsForDb, book51));

            // 52. Raffinatezza Culturale
            combinationTitle = "Raffinatezza Culturale";
            signsForDb = new ArrayList<>();
            signsToProcess = new HashMap<>();
            signsToProcess.put("Elegante", 8);
            signsToProcess.put("Fine", 8);
            signsToProcess.put("Accurata", 7);
            signsToProcess.put("Sinuosa", 7); // Abilità e fluidità espressiva
            signsForDb = processSigns(combinationTitle, signsForDb, signsToProcess);
            Book book52 = processBook((long) 6, combinationTitle);
            combinations.add(new SignCombination(null, combinationTitle, "Gusto per il bello, cultura e distinzione",
                    "Ricercatezza nelle maniere e negli interessi.", signsForDb, book52)); // Grafologia delle
                                                                                           // attitudini umane

            signCombinationRepository.saveAll(combinations);
            System.out.println("SignCombinationDatabase initialized with default Books.");
        } else {
            System.out.println("SignCombinationDatabase already initialized.");
        }
    }

    private ArrayList<ValuatedSign> processSigns(String combinationTitle, ArrayList<ValuatedSign> signsForDb,
            HashMap<String, Integer> signsToProcess) {
        ArrayList<ValuatedSign> signsForDbcopy = new ArrayList<ValuatedSign>(signsForDb);

        signsToProcess.entrySet().stream().forEach(sign -> {
            String signName = sign.getKey();
            Integer value = sign.getValue();

            Optional<Sign> optionalSign = signRepository.findByName(signName);
            if (optionalSign.isPresent()) {
                Sign foundSign = optionalSign.get();
                Long signId = foundSign.getId();
                signsForDbcopy.add(new ValuatedSign(signId, value));
            } else {
                System.err.println("ATTENZIONE: Segno con nome '" + signName
                        + "' non trovato nel database. Impossibile creare ValuatedSign per la combinazione: "
                        + combinationTitle);
            }
        });
        return signsForDbcopy;
    }

    private Book processBook(Long id, String combinationTitle) {
        Optional<Book> book = bookRepository.findById(id);
        if (book.isPresent()) {
            return book.get();
        } else {
            System.err.println("ATTENZIONE: Libro con id '" + id
                    + "' non trovato nel database. Impossibile creare la combinazione :" + combinationTitle);
        }
        return null;
    }

}
