package com.unifize.UnifizeDiscountService.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DiscountValidationResponseDto {
    private boolean valid;
    private String message;
}
