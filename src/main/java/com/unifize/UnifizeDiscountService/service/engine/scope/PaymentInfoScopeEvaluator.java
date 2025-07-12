package com.unifize.UnifizeDiscountService.service.engine.scope;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unifize.UnifizeDiscountService.exception.DiscountCalculationException;
import com.unifize.UnifizeDiscountService.exception.DiscountValidationException;
import com.unifize.UnifizeDiscountService.model.*;
import com.unifize.UnifizeDiscountService.model.engine.ConditionNode;
import com.unifize.UnifizeDiscountService.service.engine.condition.ConditionExpressionParser;
import com.unifize.UnifizeDiscountService.service.engine.condition.ConditionNodeEvaluator;
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
public class PaymentInfoScopeEvaluator implements ScopeEvaluator {

    @Autowired
    private final ObjectMapper objectMapper;

    @Override
    public boolean isDiscountApplicable(
            List<CartItem> cartItems,
            CustomerProfile customer,
            Optional<PaymentInfo> paymentInfo,
            DiscountedPrice discountedPrice,
            DiscountPolicy policy,
            String voucherCode) throws DiscountValidationException {

        if (policy.getVoucherCode() != null &&
                (voucherCode == null || !voucherCode.equals(policy.getVoucherCode()))) {
            throw new DiscountValidationException("Invalid or missing voucher code for discount: " + policy.getName());
        }

        if (paymentInfo.isEmpty()) {
            throw new DiscountValidationException("Payment information is required for this discount.");
        }

        if (policy.getConditionExpression() == null) return true;

        try {
            ConditionNode conditionNode = ConditionExpressionParser.parse(policy.getConditionExpression());

            Map<String, Object> context = objectMapper.convertValue(paymentInfo.get(), new TypeReference<>() {});
            return ConditionNodeEvaluator.evaluate(conditionNode, context);

        } catch (Exception e) {
            throw new DiscountValidationException("PAYMENT_INFO condition evaluation failed: " + e.getMessage());
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

        if (paymentInfo.isEmpty()) {
            throw new DiscountCalculationException("Payment info not present, cannot apply PAYMENT_INFO discount.");
        }

        try {
            BigDecimal finalBefore = discountedPrice.getFinalPrice();
            BigDecimal discount = getDiscount(policy, finalBefore);

            BigDecimal finalAfter = finalBefore.subtract(discount).max(BigDecimal.ZERO);
            discountedPrice.setFinalPrice(finalAfter);
            discountedPrice.getAppliedDiscounts().put(policy.getName(), discount);
            discountedPrice.getAppliedDiscountIdSet().add(policy.getId());

            return discountedPrice;

        } catch (Exception e) {
            throw new DiscountCalculationException("PAYMENT_INFO discount evaluation failed: " + e.getMessage());
        }
    }

    private static BigDecimal getDiscount(DiscountPolicy policy, BigDecimal finalBefore) {
        BigDecimal discount;

        if (policy.getType() == DiscountType.PERCENTAGE) {
            discount = finalBefore
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
}