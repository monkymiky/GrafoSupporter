package com.unipd.synclab.grafosupporter.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.unipd.synclab.grafosupporter.dto.BookDto;
import com.unipd.synclab.grafosupporter.dto.CombinationDto;
import com.unipd.synclab.grafosupporter.dto.ValuatedSignDto;
import com.unipd.synclab.grafosupporter.model.Sign;
import com.unipd.synclab.grafosupporter.model.ValuatedSign;
import com.unipd.synclab.grafosupporter.model.Combination;
import com.unipd.synclab.grafosupporter.repository.CombinationRepository;
import com.unipd.synclab.grafosupporter.repository.SignRepository;
import com.unipd.synclab.grafosupporter.repository.specifications.CombinationSpecifications;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.transaction.annotation.Transactional;
import com.unipd.synclab.grafosupporter.utility.CombinationMapper;

@Service
public class CombinationService {
    @Autowired
    SignRepository signRepository;
    @Autowired
    CombinationRepository combinationRepository;
    @Autowired
    CombinationMapper combinationResponseMapper;
    @Autowired
    FileStorageService fileStorageService;

    @Transactional(readOnly = true)
    public List<CombinationDto> getCombinations(Map<Long, Integer> searchedSign) {
        if (searchedSign == null) {
            throw new InvalidParameterException("non è possibile cercare combinazioni con una mappa nulla");
        }
        boolean allZero = true;
        for (Integer v : searchedSign.values()) {
            if (v != 0) {
                allZero = false;
                break;
            }
        }
        if (allZero || searchedSign.isEmpty()) {
            return this.getCombinationsExamples();
        }

        Specification<Combination> spec = CombinationSpecifications
                .allSignsInCombinationMustMatchCriteria(searchedSign);
        List<Combination> foundCombinations = combinationRepository.findAll(spec);

        List<CombinationDto> combinationDtos = foundCombinations.stream()
                .map(combination -> combinationResponseMapper.toCombinationResponseDto(combination))
                .collect(Collectors.toList());

        return combinationDtos;
    };

    @Transactional
    public void addCombination(Combination combination) {
        combinationRepository.save(combination);
    }

    @Transactional
    public void editCombination(Combination combinationData) {
        Combination existingCombination = combinationRepository.findById(combinationData.getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Impossible to edit - Combination not found with id: " + combinationData.getId()));

        if (existingCombination.getImagePath() != null
                && existingCombination.getImagePath() != combinationData.getImagePath()) {
            Path existingFilePath = fileStorageService.getImagePath(existingCombination.getImagePath());
            if (Files.exists(existingFilePath)) {
                fileStorageService.deleteFile(existingCombination.getImagePath());
            }
        }

        Combination combinationToEdit = combinationRepository.findById(combinationData.getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Impossible to edit - Combination not found with id: " + combinationData.getId()));
        if (combinationToEdit.getSourceBook() != null)
            throw new InvalidParameterException(
                    "non è possibile eliminare la combianzione con id=" + combinationData.getId()
                            + " perche l'autore è moretti");

        existingCombination.setTitle(combinationData.getTitle());
        existingCombination.setShortDescription(combinationData.getShortDescription());
        existingCombination.setLongDescription(combinationData.getLongDescription());
        existingCombination.setImagePath(combinationData.getImagePath());
        existingCombination.setAuthor(combinationData.getAuthor());

        existingCombination.getSigns().clear();

        if (combinationData.getSigns() != null) {
            List<Long> signIds = combinationData.getSigns().stream().map(s -> s.getSign().getId())
                    .collect(Collectors.toList());
            Map<Long, Sign> signsMap = signRepository.findAllById(signIds).stream()
                    .collect(Collectors.toMap(Sign::getId, sign -> sign));
            for (ValuatedSign signData : combinationData.getSigns()) {
                ValuatedSign newSign = new ValuatedSign();

                Sign signRef = signsMap.get(signData.getSign().getId());
                newSign.setSign(signRef);
                newSign.setMin(signData.getMin());
                newSign.setMax(signData.getMax());
                newSign.setClassification(signData.getClassification());
                newSign.setIsOptional(signData.getIsOptional());
                newSign.setCombination(existingCombination);
                existingCombination.getSigns().add(newSign);
            }
        }
    }

    @Transactional
    public void deleteCombination(Long id) {
        if (!combinationRepository.existsById(id)) {
            throw new EntityNotFoundException("Combination not found with id: " + id);
        }
        if (combinationRepository.findById(id).get().getSourceBook() != null)
            throw new InvalidParameterException(
                    "non è possibile eliminare la combianzione con id=" + id + " perche l'autore è moretti");
        combinationRepository.deleteById(id);
    }

    private ArrayList<CombinationDto> getCombinationsExamples() {
        ValuatedSignDto sign1 = new ValuatedSignDto(1L, 10, 5, "S", false, "Largo di Lettere", "");
        ValuatedSignDto sign2 = new ValuatedSignDto(2L, 5, 5, "M", false, "Curva", "Cessione");
        ValuatedSignDto sign3 = new ValuatedSignDto(11L, 5, 1, "A", true, "Angoli B", "Resistenza");
        ValuatedSignDto sign4 = new ValuatedSignDto(20L, 6, 4, "A", true, "Ascendente", "Assalto");
        ValuatedSignDto sign5 = new ValuatedSignDto(40L, 8, 6, "A", true, "Grossolana", "Attesa");
        ArrayList<ValuatedSignDto> signs1 = new ArrayList<ValuatedSignDto>();
        signs1.add(sign1);
        signs1.add(sign2);
        signs1.add(sign3);
        signs1.add(sign4);
        signs1.add(sign5);
        BookDto book = new BookDto(1L, "Manuale di grafologia", "Girolamo Moretti", 1914,
                "Edizioni Messaggero Padova",
                "9788825010008");
        CombinationDto ex1 = new CombinationDto(
                0L,
                "Titolo: Questo è un esempio di combinazione",
                "Descrizione Breve: Cliccami per visualizzare le altre informazioni",
                "Descrizione Lunga: Le informazioni che puoi visualizzare in una combianzione sono:   - I segni della combianzione con il loro intervallo in cui la combinazione ha significato: possono avere un '+' affianco, ciò significa che sono opzionali e che quindi la combianzione ha senso anche senza che questi segni siano presenti o siano nel range specificato.  Il testo di ogni sengo può essere di 5 colori che indicano il temperamento:     (nero: dipende dal contesto)      (azzurro: Cessione)      (giallo: Resistenza)       (verde: Attesa)       (rosso: assalto)   Con i bottoni bidone (rosso) e  penna (giallo) è possibile andare a eliminare o modificare una combinazione se è stata inserita dall'utente. Apri il secondo esempio per vederli!  PS: l'intervallo di grado per un segno 0-0/10 indica che il segno deve essere necessariamente assente perche la combinazione abbia significato. ",
                "qua puoi controllare la condizione testuale uguale a come l'ha scritta moretti nei suoi libri",
                "Girolamo Moretti",
                "scrittura.jpg",
                signs1,
                book);

        ArrayList<ValuatedSignDto> signs2 = new ArrayList<ValuatedSignDto>();
        signs2.add(sign1);
        signs2.add(sign2);
        CombinationDto ex2 = new CombinationDto(
                2L,
                "Questo è un altro esempio di combinazione",
                "Cliccami per visualizzare le altre informazioni",
                "Ora incomincia pure la ricerca delle combinazioni selezionando nella barra laterale il grado di tutti i segni che hai trovato durante l'analisi!  ATTENTO! se alcuni non li inserisci il sistema li considera come assenti e non ti mostrerà le combinazioni che li riguardano (a meno che non siano segni opzionali) siccome le combinazioni sono veramente tante e altrimenti sarebbe da visualizzarne sempre tantissime!",
                "",
                "Utente",
                "scrittura2.jpg",
                signs2,
                null);
        ArrayList<CombinationDto> result = new ArrayList<CombinationDto>();
        result.add(ex1);
        result.add(ex2);
        return result;
    }

}
