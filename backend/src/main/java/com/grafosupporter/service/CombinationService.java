package com.grafosupporter.service;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.transaction.annotation.Transactional;

import com.grafosupporter.dto.AuthorDto;
import com.grafosupporter.dto.BookDto;
import com.grafosupporter.dto.CombinationDto;
import com.grafosupporter.dto.ValuatedSignDto;
import com.grafosupporter.model.Combination;
import com.grafosupporter.model.Sign;
import com.grafosupporter.model.ValuatedSign;
import com.grafosupporter.repository.CombinationRepository;
import com.grafosupporter.repository.SignRepository;
import com.grafosupporter.repository.specifications.CombinationSpecifications;
import com.grafosupporter.utility.CombinationMapper;

@Service
public class CombinationService {
    private final SignRepository signRepository;
    private final CombinationRepository combinationRepository;
    private final CombinationMapper combinationResponseMapper;
    private final ImageFileService imageFileService;

    public CombinationService(
            SignRepository signRepository,
            CombinationRepository combinationRepository,
            CombinationMapper combinationResponseMapper,
            ImageFileService imageFileService) {
        this.signRepository = signRepository;
        this.combinationRepository = combinationRepository;
        this.combinationResponseMapper = combinationResponseMapper;
        this.imageFileService = imageFileService;
    }

    @Transactional(readOnly = true)
    public List<CombinationDto> getCombinations(Map<Long, Integer> searchedSign, List<String> authorCustomUsernames) {
        boolean hasSignFilters = searchedSign != null && !searchedSign.isEmpty() 
                && searchedSign.values().stream().anyMatch(v -> v != 0);
        boolean hasAuthorFilters = authorCustomUsernames != null && !authorCustomUsernames.isEmpty();
        
        if (!hasSignFilters && !hasAuthorFilters) {
            return new ArrayList<>();
        }
        
        Specification<Combination> spec = CombinationSpecifications.withFilters(searchedSign, authorCustomUsernames);
        
        List<Combination> foundCombinations = combinationRepository.findAll(spec);

        return foundCombinations.stream()
                .map(combinationResponseMapper::toCombinationResponseDto)
                .toList();
    }

    @Transactional
    public Combination addCombination(Combination combination) {
        return combinationRepository.save(combination);
    }

    @Transactional
    public Combination editCombination(Long id, Combination combinationData) throws IOException {
        Combination existingCombination = combinationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Impossible to edit - Combination not found with id: " + combinationData.getId()));

        if (existingCombination.getImagePath() != null
                && !existingCombination.getImagePath().equals(combinationData.getImagePath())) {
            imageFileService.deleteImageFile(existingCombination.getImagePath());
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
                    .toList();
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
        return combinationRepository.save(existingCombination);
    }

    @Transactional
    public void deleteCombination(Long id) {
        Optional<Combination> combiantion = combinationRepository.findById(id);
        if (combiantion.isEmpty()) {
            throw new EntityNotFoundException("Combination not found with id: " + id);
        } else if (combiantion.get().getSourceBook() != null)
            throw new InvalidParameterException(
                    "non è possibile eliminare la combianzione con id=" + id + " perche l'autore è moretti");
        combinationRepository.deleteById(id);
    }

}
