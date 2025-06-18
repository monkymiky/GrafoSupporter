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
import com.unipd.synclab.grafosupporter.service.CombinationService;
import com.unipd.synclab.grafosupporter.utility.CombinationMapper;

@RestController
@RequestMapping("/combinations")
public class CombinationController {
    @Autowired
    private CombinationService combinationService;
    @Autowired
    private CombinationMapper combinationMapper;

    @PostMapping
    public List<CombinationDto> getSignCombinationsWithoutValue(@RequestBody Map<Long, Integer> searchedSign) {
        System.out.println("Input ricevuto: " + searchedSign);
        return combinationService.getSignCombinationsWithoutValue(searchedSign);
    }

    @PostMapping
    public void addSignCombination(@RequestBody CombinationDto signCombinationDto) {
        System.out.println("debug");
        Combination signCombination = combinationMapper.toCombinationEntity(signCombinationDto);
        combinationService.addSignCombination(signCombination);
    }

    @PutMapping("/{combination_id}")
    public void editSignCombination(@RequestBody CombinationDto signCombinationDto,
            @PathVariable("combination_id") Long combination_id) {
        combinationService.editSignCombination(combinationMapper.toCombinationEntity(signCombinationDto));
    }

    @DeleteMapping("/{combination_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSignCombination(@PathVariable("combination_id") Long combination_id) {
        combinationService.deleteSignCombination(combination_id);
    }

}
