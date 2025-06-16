package com.unipd.synclab.grafosupporter.utility;

import org.springframework.stereotype.Component;

import com.unipd.synclab.grafosupporter.model.Combination;
import com.unipd.synclab.grafosupporter.model.ValuatedSign;

import java.util.List;
import java.util.stream.Collectors;

import com.unipd.synclab.grafosupporter.dto.CombinationDto;
import com.unipd.synclab.grafosupporter.dto.ValuatedSignDto;

@Component
public class SignCombinationMapper {

    private final BookMapper bookMapper;
    private final ValuatedSignMapper valuatedSignMapper;

    public SignCombinationMapper(BookMapper bookMapper, ValuatedSignMapper valuatedSignMapper) {
        this.bookMapper = bookMapper;
        this.valuatedSignMapper = valuatedSignMapper;
    }

    public CombinationDto toCombinationResponseDto(Combination combination) {
        if (combination == null) {
            return null;
        }

        CombinationDto dto = new CombinationDto();
        dto.setId(combination.getId());
        dto.setTitle(combination.getTitle());
        dto.setAuthor(combination.getAuthor());
        dto.setShortDescription(combination.getShortDescription());
        dto.setLongDescription(combination.getLongDescription());
        dto.setOriginalTextCondition(combination.getOriginalTextCondition());
        dto.setImagePath(combination.getImagePath());

        if (combination.getSourceBook() != null) {
            dto.setSourceBook(bookMapper.toBookDto(combination.getSourceBook()));
        }

        if (combination.getSigns() != null) {
            List<ValuatedSignDto> signsDto = combination.getSigns().stream()
                    .map(valuatedSignMapper::toValuatedSignDto)
                    .collect(Collectors.toList());
            dto.setSigns(signsDto);
        }

        return dto;
    }

    public Combination toCombinationEntity(CombinationDto dto) {
        if (dto == null) {
            return null;
        }

        Combination combinationEntity = new Combination();
        combinationEntity.setId(dto.getId());
        combinationEntity.setTitle(dto.getTitle());
        combinationEntity.setShortDescription(dto.getShortDescription());
        combinationEntity.setLongDescription(dto.getLongDescription());
        combinationEntity.setOriginalTextCondition(dto.getOriginalTextCondition());
        combinationEntity.setAuthor(dto.getAuthor());
        combinationEntity.setImagePath(dto.getImagePath());

        if (dto.getSourceBook() != null) {
            combinationEntity.setSourceBook(bookMapper.toBookEntity(dto.getSourceBook()));
        }

        if (dto.getSigns() != null) {

            List<ValuatedSign> valuatedSigns = dto.getSigns().stream()
                    .map(valuatedSignDto -> {
                        ValuatedSign valuatedSign = valuatedSignMapper.toValuatedSignEntity(valuatedSignDto);
                        valuatedSign.setCombination(combinationEntity);
                        return valuatedSign;
                    })
                    .collect(Collectors.toList());
            combinationEntity.setSigns(valuatedSigns);
        }

        return combinationEntity;
    }

}
