package com.unipd.synclab.grafosupporter.service;

import java.security.InvalidParameterException;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unipd.synclab.grafosupporter.model.SignCombination;
import com.unipd.synclab.grafosupporter.repository.SignCombinationRepository;
import com.unipd.synclab.grafosupporter.repository.SignRepository;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.transaction.annotation.Transactional;

@Service
public class SignCombinationService {
    @Autowired
    SignRepository signRepository;
    @Autowired
    SignCombinationRepository signCombinationRepository;

    @Transactional(readOnly = true)
    public SignCombination getSignCombinationsById(Long id) {
        Optional<SignCombination> signCombination = signCombinationRepository.findById(id);
        if (signCombination.isPresent())
            return signCombination.get();
        else
            throw new InvalidParameterException("signCombination with id = " + id + " doesnt'exist.");
    }

    @Transactional(readOnly = true)
    public List<SignCombination> getSignCombinationsWithoutValue(Map<Long, Integer> serchedSign) {
        List<SignCombination> result = signCombinationRepository.findAll();
        return result.stream()
                .filter(combination -> combination.getSigns().stream()
                        .allMatch(signInCombination -> {

                            Integer requestedSignValue = serchedSign.get(signInCombination.getSignId());
                            if (requestedSignValue == null) {
                                return false; // ignora la combinazione
                            }
                            return matchValues(requestedSignValue, signInCombination.getValue());
                        }

                        )).collect(Collectors.toList());

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
        signCombinationRepository.deleteById(id);
    }

    private boolean matchValues(Integer frontendValue, Integer backendValue) {
        switch (frontendValue) {
            case 0:
                return backendValue < 5;
            case 1:
                return backendValue.equals(5);
            case 2:
                return backendValue > 5;
            default:
                throw new InvalidParameterException();
        }
    }

}
