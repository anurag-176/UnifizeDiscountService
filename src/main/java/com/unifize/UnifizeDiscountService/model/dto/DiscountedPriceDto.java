package com.unifize.UnifizeDiscountService.model.dto;

import com.unifize.UnifizeDiscountService.model.DiscountedPrice;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DiscountedPriceDto {
    private BigDecimal originalPrice;
    private BigDecimal finalPrice;
    private Map<String, BigDecimal> appliedDiscounts;
    private String message;

    public static DiscountedPriceDto from(DiscountedPrice price) {
        return DiscountedPriceDto.builder()
                .originalPrice(price.getOriginalPrice())
                .finalPrice(price.getFinalPrice())
                .appliedDiscounts(price.getAppliedDiscounts())
                .message(price.getMessage())
                .build();
    }
}
