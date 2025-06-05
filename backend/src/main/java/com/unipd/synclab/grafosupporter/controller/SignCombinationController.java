package com.unipd.synclab.grafosupporter.controller;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.unipd.synclab.grafosupporter.model.Sign;
import com.unipd.synclab.grafosupporter.model.SignCombination;
import com.unipd.synclab.grafosupporter.service.SignCombinationService;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@RequestMapping("/combinations")
@CrossOrigin(origins = "http://localhost:4200")
public class SignCombinationController {

    private final SignCombinationService signCombinationService;

    public SignCombinationController(SignCombinationService signCombinationService) {
        this.signCombinationService = signCombinationService;
    }

    @PostMapping("/withoutNumbers")
    public List<SignCombination> getSignCombinationsWithoutValue(@RequestBody Map<Long, Integer> searchedSign) {
        System.out.println("Input ricevuto: " + searchedSign);
        return signCombinationService.getSignCombinationsWithoutValue(searchedSign);
    }

    @GetMapping("/{combination_id}")
    public SignCombination getSignCombinationsById(@PathVariable Long combination_id) {
        return signCombinationService.getSignCombinationsById(combination_id);
    }

    @PostMapping
    public void addSignCombination(@RequestBody SignCombination signCombination) {
        signCombinationService.addSignCombination(signCombination);
    }

    @PutMapping
    public void editSignCombination(@RequestBody SignCombination signCombination) {
        signCombinationService.editSignCombination(signCombination);
    }

    @DeleteMapping("/{combination_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSignCombination(@PathVariable("combination_id") Long combination_id) {
        signCombinationService.deleteSignCombination(combination_id);
    }

}
