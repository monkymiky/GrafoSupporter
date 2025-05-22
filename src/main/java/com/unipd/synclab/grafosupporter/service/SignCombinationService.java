package com.unipd.synclab.grafosupporter.service;

import java.security.InvalidParameterException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unipd.synclab.grafosupporter.model.SignCombination;
import com.unipd.synclab.grafosupporter.repository.SignCombinationRepository;
import com.unipd.synclab.grafosupporter.repository.SignRepository;

import org.springframework.transaction.annotation.Transactional;

@Service
public class SignCombinationService {
    @Autowired
    SignRepository signRepository;
    @Autowired
    SignCombinationRepository signCombinationRepository;

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
