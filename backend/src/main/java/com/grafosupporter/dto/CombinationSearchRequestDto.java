package com.grafosupporter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CombinationSearchRequestDto {
    private Map<Long, Integer> searchedSign;
    private List<String> authorCustomUsernames;
}
