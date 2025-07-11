package com.unifize.UnifizeDiscountService.model;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class CustomerProfile {
   private String id;
   private String tier;
   // Add more fields as needed
} 