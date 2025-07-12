package com.unifize.UnifizeDiscountService.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DiscountedPrice {
   private BigDecimal originalPrice;
   private BigDecimal finalPrice;
   private Map<String, BigDecimal> appliedDiscounts; // discount_name -> amount
   private Set<String> appliedDiscountIdSet;
   private String message;
} 