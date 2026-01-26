package com.grafosupporter.utility;

import org.springframework.stereotype.Component;

import com.grafosupporter.dto.AuthorDto;
import com.grafosupporter.dto.CombinationDto;
import com.grafosupporter.dto.ValuatedSignDto;
import com.grafosupporter.dto.VoteStatsDto;
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
        return toCombinationResponseDto(combination, null);
    }

    public CombinationDto toCombinationResponseDto(Combination combination, VoteStatsDto voteStats) {
        if (combination == null) {
            return null;
        }

        CombinationDto dto = new CombinationDto();
        dto.setId(combination.getId());
        dto.setTitle(combination.getTitle());
        if (combination.getAuthor() != null) {
            String authorName = combination.getAuthor().getCustomUsername() != null 
                    && !combination.getAuthor().getCustomUsername().trim().isEmpty()
                    ? combination.getAuthor().getCustomUsername()
                    : combination.getAuthor().getName();
            AuthorDto authorDto = new AuthorDto(
                combination.getAuthor().getId(),
                authorName,
                combination.getAuthor().getPictureUrl()
            );
            dto.setAuthor(authorDto);
        } else {
            dto.setAuthor(null);
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

        dto.setVoteStats(voteStats);

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

        if (dto.getAuthor() != null && dto.getAuthor().getId() != null) {
            combinationEntity.setAuthor(userService.findById(dto.getAuthor().getId()));
        } else {
            combinationEntity.setAuthor(null);
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
