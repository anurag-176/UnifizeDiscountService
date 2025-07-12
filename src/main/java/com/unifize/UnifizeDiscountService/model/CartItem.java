package com.unifize.UnifizeDiscountService.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItem {
   private Product product;
   private int quantity;
   private String size;
} 