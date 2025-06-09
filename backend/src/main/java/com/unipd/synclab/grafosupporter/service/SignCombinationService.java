package com.unipd.synclab.grafosupporter.service;

import java.security.InvalidParameterException;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.unipd.synclab.grafosupporter.dto.CombinationResponseDto;
import com.unipd.synclab.grafosupporter.dto.ValuatedSignDto;
import com.unipd.synclab.grafosupporter.model.Sign;
import com.unipd.synclab.grafosupporter.model.SignCombination;
import com.unipd.synclab.grafosupporter.repository.SignCombinationRepository;
import com.unipd.synclab.grafosupporter.repository.SignRepository;
import com.unipd.synclab.grafosupporter.repository.specifications.SignCombinationSpecifications;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.transaction.annotation.Transactional;
import com.unipd.synclab.grafosupporter.utility.SignCombinationResponseMapper;

@Service
public class SignCombinationService {
    @Autowired
    SignRepository signRepository;
    @Autowired
    SignCombinationRepository signCombinationRepository;
    @Autowired
    SignCombinationResponseMapper signCombinationResponseMapper;

    @Transactional(readOnly = true)
    public SignCombination getSignCombinationsById(Long id) {
        Optional<SignCombination> signCombination = signCombinationRepository.findById(id);
        if (signCombination.isPresent())
            return signCombination.get();
        else
            throw new InvalidParameterException("signCombination with id = " + id + " doesnt'exist.");
    }

    @Transactional(readOnly = true)
    public List<CombinationResponseDto> getSignCombinationsWithoutValue(Map<Long, Integer> serchedSign) {
        if (serchedSign == null || serchedSign.isEmpty()) {
            throw new InvalidParameterException("non è possibile cercare combinazioni che non contengono segni");
        }
        List<Sign> allSigns = signRepository.findAll();
        Specification<SignCombination> spec = SignCombinationSpecifications
                .allSignsInCombinationMustMatchCriteria(serchedSign);
        List<SignCombination> foundCombinations = signCombinationRepository.findAll(spec);

        List<List<ValuatedSignDto>> valuatedSignsDtos = foundCombinations.stream()
                .map(signCombination -> signCombination.getSigns().stream()
                        .map(sign -> {
                            Sign foundSign = allSigns.stream()
                                    .filter(s -> s.getId().equals(sign.getSignId()))
                                    .findFirst()
                                    .orElseThrow(() -> new EntityNotFoundException(
                                            "Sign not found with id: " + sign.getSignId()));
                            return new ValuatedSignDto(sign.getSignId(), sign.getMax(), sign.getMin(),
                                    sign.getClassification(), sign.getIsOptional(), foundSign.getName(),
                                    foundSign.getTemperamento());
                        })
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());

        List<CombinationResponseDto> signCombinationDtos = foundCombinations.stream()
                .map(signCombination -> signCombinationResponseMapper.toCombinationResponseDto(signCombination,
                        valuatedSignsDtos.get(foundCombinations.indexOf(signCombination))))
                .collect(Collectors.toList());

        return signCombinationDtos;
    };

    @Transactional
    public void addSignCombination(SignCombination signCombination) {
        signCombinationRepository.save(signCombination);
    }

    @Transactional
    public void editSignCombination(SignCombination signCombination) {
        SignCombination signCombinationToEdit = signCombinationRepository.findById(signCombination.getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Impossible to edit - SignCombination not found with id: " + signCombination.getId()));
        if (signCombinationToEdit.getSourceBook() != null)
            throw new InvalidParameterException(
                    "non è possibile eliminare la combianzione con id=" + signCombination.getId()
                            + " perche l'autore è moretti");
        signCombinationToEdit.setLongDescription((signCombination.getLongDescription()));
        signCombinationToEdit.setShortDescription(signCombination.getShortDescription());
        signCombinationToEdit.setSigns(signCombination.getSigns());
        signCombinationToEdit.setSourceBook(signCombination.getSourceBook());
        signCombinationToEdit.setTitle(signCombination.getTitle());
        signCombinationToEdit.setSourceBook(signCombination.getSourceBook());
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
