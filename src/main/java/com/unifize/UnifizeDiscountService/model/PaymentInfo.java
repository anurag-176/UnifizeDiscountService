package com.unifize.UnifizeDiscountService.model;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class PaymentInfo {
   private String method; // CARD, UPI, etc
   private String bankName; // Optional
   private String cardType; // Optional: CREDIT, DEBIT
} 