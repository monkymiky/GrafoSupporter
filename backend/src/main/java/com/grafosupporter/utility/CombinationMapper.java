package com.grafosupporter.utility;

import org.springframework.stereotype.Component;

import com.grafosupporter.dto.CombinationDto;
import com.grafosupporter.dto.ValuatedSignDto;
import com.grafosupporter.model.Combination;
import com.grafosupporter.model.ValuatedSign;
import com.grafosupporter.service.UserService;

import java.util.List;

@Component
public class CombinationMapper {
    private final BookMapper bookMapper;
    private final ValuatedSignMapper valuatedSignMapper;
    private final UserService userService;

    public CombinationMapper(BookMapper bookMapper, ValuatedSignMapper valuatedSignMapper, UserService userService) {
        this.bookMapper = bookMapper;
        this.valuatedSignMapper = valuatedSignMapper;
        this.userService = userService;
    }

    public CombinationDto toCombinationResponseDto(Combination combination) {
        if (combination == null) {
            return null;
        }

        CombinationDto dto = new CombinationDto();
        dto.setId(combination.getId());
        dto.setTitle(combination.getTitle());
        if (combination.getAuthor() != null) {
            dto.setAuthor(combination.getAuthor().getId());
        }
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
                    .toList();
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

        if (dto.getAuthor() != null) {
            combinationEntity.setAuthor(userService.findById(dto.getAuthor()));
        }

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
                    .toList();
            combinationEntity.setSigns(valuatedSigns);
        }

        return combinationEntity;
    }

}
