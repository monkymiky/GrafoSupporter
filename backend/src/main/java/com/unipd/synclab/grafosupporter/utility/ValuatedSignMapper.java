package com.unipd.synclab.grafosupporter.utility;

import com.unipd.synclab.grafosupporter.dto.ValuatedSignDto;
import com.unipd.synclab.grafosupporter.model.Sign;
import com.unipd.synclab.grafosupporter.model.ValuatedSign;

public class ValuatedSignMapper {

    ValuatedSignDto toValuatedSignDto(ValuatedSign valuatedSign, Sign sign) {
        return new ValuatedSignDto(valuatedSign.getSignId(), valuatedSign.getMax(), valuatedSign.getMin(),
                valuatedSign.getClassification(), valuatedSign.getIsOptional(), sign.getName(), sign.getTemperamento());
    }

}
