package com.unifize.UnifizeDiscountService.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentInfo {
   private String method; // CARD, UPI, etc
   private String bankName; // Optional
   private String cardType; // Optional: CREDIT, DEBIT
} 