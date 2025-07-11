package com.unifize.UnifizeDiscountService.service;

import com.unifize.UnifizeDiscountService.model.*;
import com.unifize.UnifizeDiscountService.exception.*;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DiscountServiceImpl implements DiscountService {
    @Override
    public DiscountedPrice calculateCartDiscounts(
            List<CartItem> cartItems,
            CustomerProfile customer,
            Optional<PaymentInfo> paymentInfo
    ) throws DiscountCalculationException {
        BigDecimal originalTotal = BigDecimal.ZERO;
        BigDecimal finalTotal = BigDecimal.ZERO;
        Map<String, BigDecimal> appliedDiscounts = new LinkedHashMap<>();
        StringBuilder message = new StringBuilder();

        // Hardcoded discount rules for demonstration
        BigDecimal pumaBrandDiscount = new BigDecimal("0.40"); // 40% off
        BigDecimal tshirtCategoryDiscount = new BigDecimal("0.10"); // 10% off
        BigDecimal iciciBankDiscount = new BigDecimal("0.10"); // 10% off
        String voucherCode = "SUPER69";
        BigDecimal voucherDiscount = new BigDecimal("0.69"); // 69% off

        // Step 1: Apply brand/category discounts
        for (CartItem item : cartItems) {
            Product product = item.getProduct();
            BigDecimal basePrice = product.getBasePrice();
            BigDecimal discountedPrice = basePrice;
            StringBuilder itemMsg = new StringBuilder();

            // Brand-specific discount
            if ("PUMA".equalsIgnoreCase(product.getBrand())) {
                BigDecimal brandDiscountAmount = basePrice.multiply(pumaBrandDiscount);
                discountedPrice = discountedPrice.subtract(brandDiscountAmount);
                appliedDiscounts.merge("PUMA Brand Discount", brandDiscountAmount.multiply(BigDecimal.valueOf(item.getQuantity())), BigDecimal::add);
                itemMsg.append("PUMA 40% off. ");
            }
            // Category-specific discount
            if ("T-shirt".equalsIgnoreCase(product.getCategory()) || "T-shirts".equalsIgnoreCase(product.getCategory())) {
                BigDecimal categoryDiscountAmount = discountedPrice.multiply(tshirtCategoryDiscount);
                discountedPrice = discountedPrice.subtract(categoryDiscountAmount);
                appliedDiscounts.merge("T-shirt Category Discount", categoryDiscountAmount.multiply(BigDecimal.valueOf(item.getQuantity())), BigDecimal::add);
                itemMsg.append("T-shirt 10% off. ");
            }
            product.setCurrentPrice(discountedPrice);
            originalTotal = originalTotal.add(basePrice.multiply(BigDecimal.valueOf(item.getQuantity())));
            finalTotal = finalTotal.add(discountedPrice.multiply(BigDecimal.valueOf(item.getQuantity())));
            if (itemMsg.length() > 0) {
                message.append(product.getBrand()).append(" ").append(product.getCategory()).append(": ").append(itemMsg).append("\n");
            }
        }

        // Step 2: Apply voucher/coupon code (assume only one code for demo)
        // For demo, check if any cart item has a voucher code attached (in real, this would come as a param)
        boolean voucherApplied = false;
        for (CartItem item : cartItems) {
            // For demo, let's assume the voucher is always applied if present
            // In real, you'd check a param or a field
            if (!voucherApplied && voucherCode.equalsIgnoreCase("SUPER69")) {
                BigDecimal voucherAmount = finalTotal.multiply(voucherDiscount);
                finalTotal = finalTotal.subtract(voucherAmount);
                appliedDiscounts.put("SUPER69 Voucher", voucherAmount);
                message.append("Voucher SUPER69 applied: 69% off on total.\n");
                voucherApplied = true;
            }
        }

        // Step 3: Apply bank offer
        if (paymentInfo.isPresent()) {
            PaymentInfo pi = paymentInfo.get();
            if ("ICICI".equalsIgnoreCase(pi.getBankName())) {
                BigDecimal bankDiscountAmount = finalTotal.multiply(iciciBankDiscount);
                finalTotal = finalTotal.subtract(bankDiscountAmount);
                appliedDiscounts.put("ICICI Bank Offer", bankDiscountAmount);
                message.append("ICICI Bank 10% instant discount applied.\n");
            }
        }

        return DiscountedPrice.builder()
                .originalPrice(originalTotal)
                .finalPrice(finalTotal)
                .appliedDiscounts(appliedDiscounts)
                .message(message.toString())
                .build();
    }

    @Override
    public boolean validateDiscountCode(String code, List<CartItem> cartItems, CustomerProfile customer) throws DiscountValidationException {
        // For demo, only 'SUPER69' is valid
        if ("SUPER69".equalsIgnoreCase(code)) {
            return true;
        }
        return false;
    }
} 