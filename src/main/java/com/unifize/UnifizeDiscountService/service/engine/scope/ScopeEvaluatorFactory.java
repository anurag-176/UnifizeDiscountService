package com.unifize.UnifizeDiscountService.service.engine.scope;

import com.unifize.UnifizeDiscountService.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

@Component
public class ScopeEvaluatorFactory {

    private final Map<DiscountScope, ScopeEvaluator> scopeMap = new EnumMap<>(DiscountScope.class);

    @Autowired
    public ScopeEvaluatorFactory(
            CartItemScopeEvaluator cartItemEvaluator,
            PaymentInfoScopeEvaluator paymentEvaluator,
            FinalSumScopeEvaluator finalSumEvaluator,
            CustomerInfoScopeEvaluator customerInfoScopeEvaluator) {

        scopeMap.put(DiscountScope.CART_ITEM, cartItemEvaluator);
        scopeMap.put(DiscountScope.PAYMENT_INFO, paymentEvaluator);
        scopeMap.put(DiscountScope.FINAL_SUM, finalSumEvaluator);
        scopeMap.put(DiscountScope.CUSTOMER_INFO, customerInfoScopeEvaluator);
    }

    public ScopeEvaluator getEvaluator(DiscountScope scope) {
        return Optional.ofNullable(scopeMap.get(scope)).orElseThrow(() -> new IllegalArgumentException("Unsupported scope: " + scope));
    }
}
