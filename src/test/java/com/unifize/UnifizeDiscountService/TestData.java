package com.unifize.UnifizeDiscountService;

import com.unifize.UnifizeDiscountService.model.CartItem;
import com.unifize.UnifizeDiscountService.model.CustomerProfile;
import com.unifize.UnifizeDiscountService.model.DiscountPolicy;
import com.unifize.UnifizeDiscountService.model.PaymentInfo;
import lombok.Data;
import lombok.Builder;
import java.util.List;

@Data
@Builder
public class TestData {
    private List<CartItem> cartItems;
    private CustomerProfile customer;
    private PaymentInfo paymentInfo;
    private List<DiscountPolicy> discountPolicies;
} 