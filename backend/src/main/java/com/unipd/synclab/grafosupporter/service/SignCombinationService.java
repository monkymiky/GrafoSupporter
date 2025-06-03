package com.unipd.synclab.grafosupporter.service;

import java.security.InvalidParameterException;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.unipd.synclab.grafosupporter.model.SignCombination;
import com.unipd.synclab.grafosupporter.repository.SignCombinationRepository;
import com.unipd.synclab.grafosupporter.repository.SignRepository;
import com.unipd.synclab.grafosupporter.repository.specifications.SignCombinationSpecifications;

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
        if (serchedSign == null || serchedSign.isEmpty()) {
            throw new InvalidParameterException("non è possibile cercare combianzioni che non contengono segni");
        }
        Specification<SignCombination> spec = SignCombinationSpecifications
                .allSignsInCombinationMustMatchCriteria(serchedSign);
        return signCombinationRepository.findAll(spec);
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
