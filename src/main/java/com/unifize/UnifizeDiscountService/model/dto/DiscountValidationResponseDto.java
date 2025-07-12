package com.unifize.UnifizeDiscountService.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiscountValidationResponseDto {
    private boolean valid;
    private String message;
}
