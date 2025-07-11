package com.unifize.UnifizeDiscountService.model;

import lombok.Data;
import lombok.Builder;
import java.math.BigDecimal;
import com.unifize.UnifizeDiscountService.model.BrandTier;

@Data
@Builder
public class Product {
   private String id;
   private String brand;
   private BrandTier brandTier;
   private String category;
   private BigDecimal basePrice;
   private BigDecimal currentPrice; // After brand/category discount
} 