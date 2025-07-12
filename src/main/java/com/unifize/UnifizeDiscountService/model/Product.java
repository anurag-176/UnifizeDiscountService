package com.unifize.UnifizeDiscountService.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Builder;
import java.math.BigDecimal;
import com.unifize.UnifizeDiscountService.model.BrandTier;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {
   private String id;
   private String brand;
   private BrandTier brandTier;
   private String category;
   private BigDecimal basePrice;
   private BigDecimal currentPrice; // After brand/category discount
} 