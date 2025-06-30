package com.unipd.synclab.grafosupporter.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import jakarta.validation.Valid;

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
    public ResponseEntity<List<CombinationDto>> getCombinations(@RequestBody Map<Long, Integer> searchedSign) {
        List<CombinationDto> combinations = combinationService.getCombinations(searchedSign);
        return ResponseEntity.ok(combinations);
    }

    @PostMapping
    public ResponseEntity<CombinationDto> addCombination(@Valid @RequestBody CombinationDto combinationDto) {
        Combination savedCombination = combinationService
                .addCombination(combinationMapper.toCombinationEntity(combinationDto));
        CombinationDto savedCombinationDto = combinationMapper.toCombinationResponseDto(savedCombination);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedCombination.getId())
                .toUri();
        return ResponseEntity.created(location).body(savedCombinationDto);
    }

    @PutMapping("/{combinationId}")
    public ResponseEntity<CombinationDto> editCombination(
            @PathVariable("combinationId") Long combinationId,
            @Valid @RequestBody CombinationDto combinationDto) {
        Combination combinationToUpdate = combinationMapper.toCombinationEntity(combinationDto);
        Combination updatedCombination = combinationService.editCombination(combinationId, combinationToUpdate);
        CombinationDto updatedDto = combinationMapper.toCombinationResponseDto(updatedCombination);
        return ResponseEntity.ok(updatedDto);
    }

    @DeleteMapping("/{combinationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCombination(@PathVariable("combinationId") Long combinationId) {
        combinationService.deleteCombination(combinationId);
    }

}
