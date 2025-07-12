package com.unifize.UnifizeDiscountService;

import com.unifize.UnifizeDiscountService.model.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.math.BigDecimal;
import java.util.List;

@Configuration
public class TestDataConfig {

    @Bean
    public TestData pumaBrandData() {
        CartItem cartItem = new CartItem(
                new Product("PROD3", "PUMA", BrandTier.PREMIUM, "Shoes", new BigDecimal("4000"), null),
                1, null
        );
        CustomerProfile customer = new CustomerProfile("C002", "Anu", "SILVER");

        DiscountPolicy pumaPolicy = DiscountPolicy.builder()
                .id("POLICY1")
                .name("PUMA Brand 40% Off")
                .scope(DiscountScope.CART_ITEM)
                .type(DiscountType.PERCENTAGE)
                .value(40)
                .conditionExpression("$product.brand == \"PUMA\"")
                .targetStrategy(DiscountTargetStrategyEnum.FIRST_MATCH)
                .stackOrder(1)
                .isApplyToAll(false)
                .applicationCount(1)
                .build();

        return new TestData(
                List.of(cartItem),
                customer,
                null,
                List.of(pumaPolicy)
        );
    }

    @Bean
    public TestData iciciBankData() {
        CartItem cartItem = new CartItem(
                new Product("PROD4", "Adidas", BrandTier.REGULAR, "Pants", new BigDecimal("1200"), null),
                1,
                null
        );

        CustomerProfile customer = new CustomerProfile("C003", "Rohit", "GOLD");

        PaymentInfo paymentInfo = new PaymentInfo("CARD", "ICICI", "CREDIT");

        DiscountPolicy iciciPolicy = DiscountPolicy.builder()
                .id("POLICY2")
                .name("ICICI Bank Card Offer")
                .scope(DiscountScope.CART_ITEM)
                .type(DiscountType.PERCENTAGE)
                .value(10)
                .conditionExpression("$payment.method == \"CARD\" && $payment.bankName == \"ICICI\" && $payment.cardType == \"CREDIT\"")
                .targetStrategy(DiscountTargetStrategyEnum.FIRST_MATCH)
                .stackOrder(2)
                .isApplyToAll(false)
                .applicationCount(1)
                .build();

        return new TestData(
                List.of(cartItem),
                customer,
                paymentInfo,
                List.of(iciciPolicy)
        );
    }

    @Bean
    public TestData tenPercentOffData() {
        CartItem cartItem = new CartItem(
                new Product("PROD4", "Adidas", BrandTier.REGULAR, "T-Shirt", new BigDecimal("1200"), null),
                1,
                null
        );

        CustomerProfile customer = new CustomerProfile("C003", "Rohit", "GOLD");

        DiscountPolicy tshirtPolicy = DiscountPolicy.builder()
                .id("3808c4d8-8c06-4f1f-b1bf-afada8e2ddf3")
                .name("Extra 10% off on T-shirts")
                .scope(DiscountScope.CART_ITEM)
                .type(DiscountType.PERCENTAGE)
                .value(10)
                .conditionExpression("$product.category == \"T-Shirt\"")
                .applicationCount(null)
                .maximumDiscountAmount(null)
                .voucherCode(null)
                .targetStrategy(DiscountTargetStrategyEnum.FIRST_MATCH)
                .stackOrder(3)
                .isApplyToAll(false)
                .build();

        return new TestData(
                List.of(cartItem),
                customer,
                null,
                List.of(tshirtPolicy)
        );
    }

    @Bean
    public TestData super69VoucherData() {
        List<CartItem> cartItems = List.of(
                new CartItem(
                        new Product("PROD1", "PUMA", BrandTier.PREMIUM, "Pants", new BigDecimal("300"), null),
                        1,
                        null
                ),
                new CartItem(
                        new Product("PROD2", "NIKE", BrandTier.REGULAR, "Shoes", new BigDecimal("2000"), null),
                        1,
                        null
                )
        );

        CustomerProfile customer = new CustomerProfile("C001", "Anurag", "REGULAR");

        DiscountPolicy super69 = DiscountPolicy.builder()
                .id("5cb6b575-ac94-4682-8ab6-725c228877be")
                .name("SUPER69 Voucher")
                .scope(DiscountScope.CART_ITEM)
                .type(DiscountType.PERCENTAGE)
                .value(69)
                .conditionExpression("true")
                .applicationCount(1)
                .maximumDiscountAmount(null)
                .voucherCode("SUPER69")
                .targetStrategy(DiscountTargetStrategyEnum.MIN_PRICE)
                .stackOrder(0)
                .isApplyToAll(false)
                .build();

        return new TestData(
                cartItems,
                customer,
                null,  // No payment info required
                List.of(super69)
        );
    }

    @Bean
    public TestData customerTierGoldDiscountData() {
        CartItem cartItem = new CartItem(
                new Product("PROD9", "NIKE", BrandTier.REGULAR, "Jacket", new BigDecimal("2500"), null),
                1,
                null
        );

        CustomerProfile customer = new CustomerProfile("C009", "GOLD", "Riya");

        DiscountPolicy goldMemberPolicy = DiscountPolicy.builder()
                .id("6e3f0758-60b4-44a1-965a-7e05a9112cb5")
                .name("12% Off for Gold Members")
                .scope(DiscountScope.CUSTOMER_INFO)
                .type(DiscountType.PERCENTAGE)
                .value(12)
                .conditionExpression("$tier == \"GOLD\"")
                .targetStrategy(DiscountTargetStrategyEnum.FIRST_MATCH)
                .stackOrder(4)
                .isApplyToAll(false)
                .applicationCount(null)
                .maximumDiscountAmount(null)
                .voucherCode(null)
                .mutuallyExclusiveWith(null)
                .build();

        return new TestData(
                List.of(cartItem),
                customer,
                null,
                List.of(goldMemberPolicy)
        );
    }
}
