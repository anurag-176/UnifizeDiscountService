package com.unifize.UnifizeDiscountService.service.engine.scope;

import com.unifize.UnifizeDiscountService.exception.DiscountCalculationException;
import com.unifize.UnifizeDiscountService.exception.DiscountValidationException;
import com.unifize.UnifizeDiscountService.model.*;
import java.util.List;
import java.util.Optional;

public interface ScopeEvaluator {
    DiscountedPrice evaluate(
            List<CartItem> cartItems,
            CustomerProfile customer,
            Optional<PaymentInfo> paymentInfo,
            DiscountedPrice discountedPrice,
            DiscountPolicy policy,
            String voucherCode
    ) throws DiscountCalculationException;

    boolean isDiscountApplicable(
            List<CartItem> cartItems,
            CustomerProfile customer,
            Optional<PaymentInfo> paymentInfo,
            DiscountedPrice discountedPrice,
            DiscountPolicy policy,
            String voucherCode
    ) throws DiscountValidationException;
}
