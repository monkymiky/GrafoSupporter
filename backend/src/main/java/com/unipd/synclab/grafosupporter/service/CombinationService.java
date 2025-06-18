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
import com.unipd.synclab.grafosupporter.repository.specifications.SignCombinationSpecifications;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.transaction.annotation.Transactional;
import com.unipd.synclab.grafosupporter.utility.CombinationMapper;

@Service
public class CombinationService {
    @Autowired
    SignRepository signRepository;
    @Autowired
    CombinationRepository signCombinationRepository;
    @Autowired
    CombinationMapper signCombinationResponseMapper;
    @Autowired
    FileStorageService fileStorageService;

    @Transactional(readOnly = true)
    public Combination getSignCombinationsById(Long id) {
        Optional<Combination> signCombination = signCombinationRepository.findById(id);
        if (signCombination.isPresent())
            return signCombination.get();
        else
            throw new InvalidParameterException("signCombination with id = " + id + " doesnt'exist.");
    }

    @Transactional(readOnly = true)
    public List<CombinationDto> getSignCombinationsWithoutValue(Map<Long, Integer> serchedSign) {
        if (serchedSign == null || serchedSign.isEmpty()) {
            throw new InvalidParameterException("non è possibile cercare combinazioni che non contengono segni");
        }
        // List<Sign> allSigns = signRepository.findAll();
        Specification<Combination> spec = SignCombinationSpecifications
                .allSignsInCombinationMustMatchCriteria(serchedSign);
        List<Combination> foundCombinations = signCombinationRepository.findAll(spec);

        // List<List<ValuatedSignDto>> valuatedSignsDtos = foundCombinations.stream()
        // .map(signCombination -> signCombination.getSigns().stream()
        // .map(sign -> {
        // Sign foundSign = allSigns.stream()
        // .filter(s -> s.getId().equals(sign.getSign().getId()))
        // .findFirst()
        // .orElseThrow(() -> new EntityNotFoundException(
        // "Sign not found with id: " + sign.getSign().getId()));
        // return new ValuatedSignDto(sign.getSign().getId(), sign.getMax(),
        // sign.getMin(),
        // sign.getClassification(), sign.getIsOptional(), foundSign.getName(),
        // foundSign.getTemperamento());
        // })
        // .collect(Collectors.toList()))
        // .collect(Collectors.toList());

        List<CombinationDto> signCombinationDtos = foundCombinations.stream()
                .map(signCombination -> signCombinationResponseMapper.toCombinationResponseDto(signCombination))
                .collect(Collectors.toList());

        return signCombinationDtos;
    };

    @Transactional
    public void addSignCombination(Combination signCombination) {
        signCombinationRepository.save(signCombination);
    }

    @Transactional
    public void editSignCombination(Combination combinationData) {
        Combination existingCombination = signCombinationRepository.findById(combinationData.getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Impossible to edit - SignCombination not found with id: " + combinationData.getId()));

        if (existingCombination.getImagePath() != null
                && existingCombination.getImagePath() != combinationData.getImagePath()) {
            Path existingFilePath = fileStorageService.getImagePath(existingCombination.getImagePath());
            if (Files.exists(existingFilePath)) {
                fileStorageService.deleteFile(existingCombination.getImagePath());
            }
        }

        Combination signCombinationToEdit = signCombinationRepository.findById(combinationData.getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Impossible to edit - SignCombination not found with id: " + combinationData.getId()));
        if (signCombinationToEdit.getSourceBook() != null)
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
            for (ValuatedSign signData : combinationData.getSigns()) {
                ValuatedSign newSign = new ValuatedSign();

                Sign signRef = signRepository.findById(signData.getSign().getId())
                        .orElseThrow(
                                () -> new EntityNotFoundException("Sign not found: " + signData.getSign().getId()));

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
    public void deleteSignCombination(Long id) {
        if (!signCombinationRepository.existsById(id)) {
            throw new EntityNotFoundException("SignCombination not found with id: " + id);
        }
        if (signCombinationRepository.findById(id).get().getSourceBook() != null)
            throw new InvalidParameterException(
                    "non è possibile eliminare la combianzione con id=" + id + " perche l'autore è moretti");
        signCombinationRepository.deleteById(id);
    }

}
