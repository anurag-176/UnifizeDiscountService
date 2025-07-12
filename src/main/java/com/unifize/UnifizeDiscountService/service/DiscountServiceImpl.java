package com.unifize.UnifizeDiscountService.service;

import com.unifize.UnifizeDiscountService.model.*;
import com.unifize.UnifizeDiscountService.exception.*;
import com.unifize.UnifizeDiscountService.repository.DiscountPolicyRepository;
import com.unifize.UnifizeDiscountService.service.engine.scope.ScopeEvaluator;
import com.unifize.UnifizeDiscountService.service.engine.scope.ScopeEvaluatorFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiscountServiceImpl implements DiscountService {

    @Autowired
    private DiscountPolicyRepository discountPolicyRepository;

    @Autowired
    private ScopeEvaluatorFactory scopeEvaluatorFactory;

    @Override
    public DiscountedPrice calculateCartDiscounts(
            List<CartItem> cartItems,
            CustomerProfile customer,
            Optional<PaymentInfo> paymentInfo
    ) throws DiscountCalculationException {

        // Start with original total
        BigDecimal originalTotal = cartItems.stream()
                .map(item -> item.getProduct().getBasePrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        DiscountedPrice discountedPrice = DiscountedPrice.builder()
                .originalPrice(originalTotal)
                .finalPrice(originalTotal)
                .appliedDiscounts(new LinkedHashMap<>()) // maintain order
                .appliedDiscountIdSet(new HashSet<>())
                .message("Discounts calculated")
                .build();

        // Step 1: Get all policies ordered by stackOrder
        List<DiscountPolicy> policies = StreamSupport
                .stream(discountPolicyRepository.findAll().spliterator(), false)
                .sorted(Comparator.comparingInt(DiscountPolicy::getStackOrder))
                .toList();

        for (DiscountPolicy policy : policies) {
            try {
                if (policy.getMutuallyExclusiveWith() != null &&
                        !Collections.disjoint(discountedPrice.getAppliedDiscountIdSet(), policy.getMutuallyExclusiveWith())) {
                    continue; // skip policy
                }


                ScopeEvaluator evaluator = scopeEvaluatorFactory.getEvaluator(policy.getScope());
                boolean applicable = evaluator.isDiscountApplicable(
                        cartItems, customer, paymentInfo, discountedPrice, policy, null
                );
                if (applicable) {
                    discountedPrice = evaluator.evaluate(
                            cartItems, customer, paymentInfo, discountedPrice, policy, null
                    );
                }
            } catch (DiscountValidationException | DiscountCalculationException e) {
                // Log and skip to next policy
                 log.warn("Skipping policy {}: {}", policy.getName(), e.getMessage());
            }
        }

        return discountedPrice;
    }

    @Override
    public boolean validateDiscountCode(
            String code,
            List<CartItem> cartItems,
            CustomerProfile customer
    ) throws DiscountValidationException {

        Optional<DiscountPolicy> optionalPolicy = discountPolicyRepository.findByVoucherCode(code);
        if (optionalPolicy.isEmpty()) {
            throw new DiscountValidationException("Invalid discount code: " + code);
        }

        DiscountPolicy policy = optionalPolicy.get();

        ScopeEvaluator evaluator = scopeEvaluatorFactory.getEvaluator(policy.getScope());

        // Use empty paymentInfo
        return evaluator.isDiscountApplicable(
                cartItems, customer, Optional.empty(),
                DiscountedPrice.builder().build(),
                policy, code
        );
    }
} 