package com.unifize.UnifizeDiscountService.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerProfile {
   private String id;
   private String tier;
   private String name;
   // Add more fields as needed
} 