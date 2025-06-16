package com.unipd.synclab.grafosupporter.controller;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.unipd.synclab.grafosupporter.dto.CombinationDto;
import com.unipd.synclab.grafosupporter.model.Combination;
import com.unipd.synclab.grafosupporter.service.FileStorageService;
import com.unipd.synclab.grafosupporter.service.SignCombinationService;
import com.unipd.synclab.grafosupporter.utility.SignCombinationMapper;

import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@RequestMapping("/combinations")
@CrossOrigin(origins = "http://localhost:4200")
public class SignCombinationController {
    @Autowired
    private SignCombinationService signCombinationService;
    @Autowired
    private SignCombinationMapper signCombinationMapper;
    @Autowired
    FileStorageService fileStorageService;

    @PostMapping("/withoutNumbers")
    public List<CombinationDto> getSignCombinationsWithoutValue(@RequestBody Map<Long, Integer> searchedSign) {
        System.out.println("Input ricevuto: " + searchedSign);
        return signCombinationService.getSignCombinationsWithoutValue(searchedSign);
    }

    @GetMapping("/{combination_id}")
    public Combination getSignCombinationsById(@PathVariable Long combination_id) {
        return signCombinationService.getSignCombinationsById(combination_id);
    }

    @PostMapping
    public void addSignCombination(@RequestBody CombinationDto signCombinationDto) {
        System.out.println("debug");
        Combination signCombination = signCombinationMapper.toCombinationEntity(signCombinationDto);
        signCombinationService.addSignCombination(signCombination);
    }

    @PutMapping
    public void editSignCombination(@RequestBody Combination signCombination) {
        signCombinationService.editSignCombination(signCombination);
    }

    @DeleteMapping("/{combination_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSignCombination(@PathVariable("combination_id") Long combination_id) {
        signCombinationService.deleteSignCombination(combination_id);
    }

}
