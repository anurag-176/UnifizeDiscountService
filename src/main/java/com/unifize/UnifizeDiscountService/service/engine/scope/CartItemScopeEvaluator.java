package com.unifize.UnifizeDiscountService.service.engine.scope;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unifize.UnifizeDiscountService.exception.DiscountCalculationException;
import com.unifize.UnifizeDiscountService.exception.DiscountValidationException;
import com.unifize.UnifizeDiscountService.model.*;
import com.unifize.UnifizeDiscountService.model.engine.ConditionNode;
import com.unifize.UnifizeDiscountService.service.engine.condition.ConditionExpressionParser;
import com.unifize.UnifizeDiscountService.service.engine.condition.ConditionNodeEvaluator;
import com.unifize.UnifizeDiscountService.service.engine.targetStrategy.DiscountTargetStrategy;
import com.unifize.UnifizeDiscountService.service.engine.targetStrategy.DiscountTargetStrategyFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CartItemScopeEvaluator implements ScopeEvaluator {

    @Autowired
    private DiscountTargetStrategyFactory discountTargetStrategyFactory;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public boolean isDiscountApplicable(
            List<CartItem> cartItems,
            CustomerProfile customer,
            Optional<PaymentInfo> paymentInfo,
            DiscountedPrice discountedPrice,
            DiscountPolicy policy,
            String voucherCode
    ) throws DiscountValidationException {

        if (cartItems == null || cartItems.isEmpty()) {
            throw new DiscountValidationException("Cart is empty — cannot apply CART_ITEM discount.");
        }

        if (policy.getVoucherCode() != null &&
                (voucherCode == null || !voucherCode.equals(policy.getVoucherCode()))) {
            throw new DiscountValidationException("Invalid or missing voucher code for discount: " + policy.getName());
        }

        // If no expression present, assume applicable
        if (policy.getConditionExpression() == null) return true;

        try {
            ConditionNode conditionNode = ConditionExpressionParser.parse(policy.getConditionExpression());

            // Check if at least one cart item satisfies the condition
            boolean anyMatch = cartItems.stream().anyMatch(cartItem -> {
                Map<String, Object> context = cartItemToContext(cartItem, customer, paymentInfo);
                return ConditionNodeEvaluator.evaluate(conditionNode, context);
            });

//            if (!anyMatch) {
//                throw new DiscountValidationException("No cart items matched condition for discount: " + policy.getName());
//            }
//
//            return true;

            return anyMatch;

        } catch (Exception e) {
            throw new DiscountValidationException("Failed to validate CART_ITEM condition: " + e.getMessage());
        }
    }

    @Override
    public DiscountedPrice evaluate(
            List<CartItem> cartItems,
            CustomerProfile customer,
            Optional<PaymentInfo> paymentInfo,
            DiscountedPrice discountedPrice,
            DiscountPolicy policy,
            String voucherCode) throws DiscountCalculationException {

        try {
            ConditionNode conditionNode;
            if (policy.getConditionExpression() != null) {
                conditionNode = ConditionExpressionParser.parse(policy.getConditionExpression());
            } else {
                conditionNode = null;
            }

            // 1. Filter items matching the condition
            List<CartItem> applicableItems = cartItems.stream().filter(cartItem -> {
                if (conditionNode == null) return true;
                Map<String, Object> context = cartItemToContext(cartItem, customer, paymentInfo);
                return ConditionNodeEvaluator.evaluate(conditionNode, context);
            }).toList();

            if (applicableItems.isEmpty()) return discountedPrice;

            // 2. Apply strategy to choose subset of applicable items
            DiscountTargetStrategy strategy = discountTargetStrategyFactory.getStrategy(policy.getTargetStrategy());
            List<CartItem> targetItems = strategy.apply(applicableItems);

            // 3. Limit by applicationCount if not applyToAll
            int limit = policy.isApplyToAll() ? targetItems.size()
                    : Optional.ofNullable(policy.getApplicationCount()).orElse(1);
            targetItems = targetItems.stream().limit(limit).toList();

            BigDecimal totalDiscount = BigDecimal.ZERO;

            for (CartItem item : targetItems) {
                BigDecimal originalPrice = item.getProduct().getBasePrice();
                BigDecimal discount = getDiscount(policy, originalPrice);

                BigDecimal newPrice = originalPrice.subtract(discount).max(BigDecimal.ZERO);
                item.getProduct().setCurrentPrice(newPrice);
                totalDiscount = totalDiscount.add(discount);
            }

            discountedPrice.setFinalPrice(discountedPrice.getFinalPrice().subtract(totalDiscount));
            discountedPrice.getAppliedDiscounts().put(policy.getName(), totalDiscount);

            return discountedPrice;

        } catch (Exception e) {
            throw new DiscountCalculationException("CART_ITEM evaluation failed: " + e.getMessage());
        }
    }

    private static BigDecimal getDiscount(DiscountPolicy policy, BigDecimal originalPrice) {
        BigDecimal discount;

        if (policy.getType() == DiscountType.PERCENTAGE) {
            discount = originalPrice
                    .multiply(BigDecimal.valueOf(policy.getValue()))
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

            if (policy.getMaximumDiscountAmount() != null) {
                discount = discount.min(BigDecimal.valueOf(policy.getMaximumDiscountAmount()));
            }
        } else {
            discount = BigDecimal.valueOf(policy.getValue());
        }
        return discount;
    }

    private Map<String, Object> cartItemToContext(CartItem item, CustomerProfile customer, Optional<PaymentInfo> paymentInfo) {
        return objectMapper.convertValue(item, new TypeReference<>() {});
    }
}