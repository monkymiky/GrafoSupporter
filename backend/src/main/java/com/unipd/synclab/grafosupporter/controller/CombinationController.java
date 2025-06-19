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

    @PostMapping("/get")
    public List<CombinationDto> getCombinations(@RequestBody Map<Long, Integer> searchedSign) {
        System.out.println("Input ricevuto: " + searchedSign);
        return combinationService.getCombinations(searchedSign);
    }

    @PostMapping("/add")
    public void addCombination(@RequestBody CombinationDto combinationDto) {
        System.out.println("debug");
        Combination combination = combinationMapper.toCombinationEntity(combinationDto);
        combinationService.addCombination(combination);
    }

    @PutMapping("/{combination_id}")
    public void editCombination(@RequestBody CombinationDto combinationDto,
            @PathVariable("combination_id") Long combination_id) {
        combinationService.editCombination(combinationMapper.toCombinationEntity(combinationDto));
    }

    @DeleteMapping("/{combination_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCombination(@PathVariable("combination_id") Long combination_id) {
        combinationService.deleteCombination(combination_id);
    }

}
