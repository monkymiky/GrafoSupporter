package com.grafosupporter.utility;

import org.springframework.stereotype.Component;

import com.grafosupporter.dto.ValuatedSignDto;
import com.grafosupporter.model.Sign;
import com.grafosupporter.model.ValuatedSign;
import com.grafosupporter.repository.SignRepository;

import jakarta.persistence.EntityNotFoundException;

@Component
public class ValuatedSignMapper {
    private final SignRepository signRepository;

    public ValuatedSignMapper(SignRepository signRepository) {
        this.signRepository = signRepository;
    }

    public ValuatedSignDto toValuatedSignDto(ValuatedSign valuatedSign) {
        Sign sign = valuatedSign.getSign();
        return new ValuatedSignDto(
                sign.getId(),
                valuatedSign.getMax(),
                valuatedSign.getMin(),
                valuatedSign.getClassification(),
                valuatedSign.getIsOptional(),
                sign.getName(),
                sign.getTemperamento());
    }

    public ValuatedSign toValuatedSignEntity(ValuatedSignDto dto) {
        if (dto == null) {
            return null;
        }

        Sign sign = signRepository.findById(dto.getSignId())
                .orElseThrow(() -> new EntityNotFoundException("Sign not found with id: " + dto.getSignId()));

        ValuatedSign valuatedSign = new ValuatedSign();
        valuatedSign.setSign(sign);
        valuatedSign.setMin(dto.getMin());
        valuatedSign.setMax(dto.getMax());
        valuatedSign.setIsOptional(dto.getIsOptional());
        valuatedSign.setClassification(dto.getClassification());

        return valuatedSign;
    }

}
