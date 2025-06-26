package com.unipd.synclab.grafosupporter.controller;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
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
    private final CombinationService combinationService;
    private final CombinationMapper combinationMapper;

    public CombinationController(CombinationService combinationService, CombinationMapper combinationMapper) {
        this.combinationService = combinationService;
        this.combinationMapper = combinationMapper;
    }

    @PostMapping("/search")
    public List<CombinationDto> getCombinations(@RequestBody Map<Long, Integer> searchedSign) {
        return combinationService.getCombinations(searchedSign);
    }

    @PostMapping
    public void addCombination(@RequestBody CombinationDto combinationDto) {
        Combination combination = combinationMapper.toCombinationEntity(combinationDto);
        combinationService.addCombination(combination);
    }

    @PutMapping("/{combinationId}")
    public void editCombination(@RequestBody CombinationDto combinationDto,
            @PathVariable("combinationId") Long combinationId) {
        combinationService.editCombination(combinationMapper.toCombinationEntity(combinationDto));
    }

    @DeleteMapping("/{combinationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCombination(@PathVariable("combinationId") Long combinationId) {
        combinationService.deleteCombination(combinationId);
    }

}
