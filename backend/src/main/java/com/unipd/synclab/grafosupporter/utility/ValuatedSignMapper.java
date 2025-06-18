package com.unipd.synclab.grafosupporter.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.unipd.synclab.grafosupporter.dto.ValuatedSignDto;
import com.unipd.synclab.grafosupporter.model.Sign;
import com.unipd.synclab.grafosupporter.model.ValuatedSign;
import com.unipd.synclab.grafosupporter.repository.SignRepository;

import jakarta.persistence.EntityNotFoundException;

@Component
public class ValuatedSignMapper {
    @Autowired
    private SignRepository signRepository;

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
