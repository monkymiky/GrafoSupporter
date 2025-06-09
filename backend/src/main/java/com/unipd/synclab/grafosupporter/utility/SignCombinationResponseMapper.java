package com.unipd.synclab.grafosupporter.utility;

import org.springframework.stereotype.Component;

import com.unipd.synclab.grafosupporter.model.SignCombination;

import java.util.List;
import com.unipd.synclab.grafosupporter.dto.CombinationResponseDto;
import com.unipd.synclab.grafosupporter.dto.ValuatedSignDto;

@Component
public class SignCombinationResponseMapper {

    public CombinationResponseDto toCombinationResponseDto(SignCombination combination,
            List<ValuatedSignDto> signs) {
        CombinationResponseDto dto = new CombinationResponseDto();
        dto.setId(combination.getId());
        dto.setTitle(combination.getTitle());
        dto.setAuthor(combination.getAuthor());
        dto.setShortDescription(combination.getShortDescription());
        dto.setLongDescription(combination.getLongDescription());
        dto.setOriginalTextCondition(combination.getOriginalTextCondition());
        dto.setImagePath(combination.getImagePath());
        dto.setSourceBook(combination.getSourceBook());
        dto.setSigns(signs);

        return dto;
    }

}
