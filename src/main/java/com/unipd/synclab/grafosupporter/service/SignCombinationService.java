package com.unipd.synclab.grafosupporter.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unipd.synclab.grafosupporter.model.Sign;
import com.unipd.synclab.grafosupporter.model.SignCombination;
import com.unipd.synclab.grafosupporter.model.ValuatedSign;
import com.unipd.synclab.grafosupporter.repository.SignCombinationRepository;
import com.unipd.synclab.grafosupporter.repository.SignRepository;

import org.springframework.transaction.annotation.Transactional;

@Service
public class SignCombinationService {
    private final SignCombinationRepository signCombinationRepository;
    @Autowired
    SignRepository signRepository;

    @Autowired
    public SignCombinationService(SignCombinationRepository signCombinationRepository) {
        this.signCombinationRepository = signCombinationRepository;
    }

    @Transactional(readOnly = true)
    public List<SignCombination> getSignCombinationsWithoutValue(Map<Long, Integer> serchedSign) {
        List<SignCombination> result = signCombinationRepository.findAll();
        return result.parallelStream()
                .filter(combination -> combination.getSigns().stream()
                        .allMatch(valuatedSign -> matchValues(
                                serchedSign.entrySet().stream().filter(
                                        SSign -> SSign.getKey().equals(valuatedSign.getSignId())).findFirst().get()
                                        .getValue(),
                                valuatedSign.getValue())

                        )).collect(Collectors.toList());

    };

    private boolean matchValues(Integer frontendValue, Integer backendValue) {
        return frontendValue.equals(backendValue); // da cambiare con = 5 <5 >5
    }

}
