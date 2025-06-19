package com.unipd.synclab.grafosupporter.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidParameterException;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.unipd.synclab.grafosupporter.dto.CombinationDto;
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
    public List<CombinationDto> getCombinations(Map<Long, Integer> serchedSign) {
        if (serchedSign == null || serchedSign.isEmpty()) {
            throw new InvalidParameterException("non è possibile cercare combinazioni che non contengono segni");
        }

        Specification<Combination> spec = CombinationSpecifications
                .allSignsInCombinationMustMatchCriteria(serchedSign);
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

}
