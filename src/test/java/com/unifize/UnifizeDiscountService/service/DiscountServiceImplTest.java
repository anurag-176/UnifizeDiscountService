package com.unifize.UnifizeDiscountService.service;

import com.unifize.UnifizeDiscountService.TestData;
import com.unifize.UnifizeDiscountService.TestDataConfig;
import com.unifize.UnifizeDiscountService.model.*;
import com.unifize.UnifizeDiscountService.repository.DiscountPolicyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class DiscountServiceImplTest {

    @Autowired
    private DiscountServiceImpl discountService;

    @Autowired
    private DiscountPolicyRepository discountPolicyRepository;

    @Autowired
    private TestDataConfig testDataConfig;

    @Test
    void testCalculateCartDiscounts_PumaScenario() {
        TestData data = testDataConfig.pumaBrandData(); // Load 1st test data

        DiscountedPrice result = discountService.calculateCartDiscounts(
                data.getCartItems(),
                data.getCustomer(),
                Optional.ofNullable(data.getPaymentInfo())
        );

        assertTrue(result.getAppliedDiscounts().containsKey("PUMA Brand 40% Off"));
    }

    @Test
    void testCalculateCartDiscounts_IciciScenario() {
        TestData data = testDataConfig.iciciBankData(); // Load 1st test data

        DiscountedPrice result = discountService.calculateCartDiscounts(
                data.getCartItems(),
                data.getCustomer(),
                Optional.ofNullable(data.getPaymentInfo())
        );

        assertTrue(result.getAppliedDiscounts().containsKey("ICICI Bank Card Offer"));
    }

    @Test
    void testCalculateCartDiscounts_tenPercentOffOnShirts() {
        TestData data = testDataConfig.tenPercentOffData(); // Load 1st test data

        DiscountedPrice result = discountService.calculateCartDiscounts(
                data.getCartItems(),
                data.getCustomer(),
                Optional.ofNullable(data.getPaymentInfo())
        );

        assertTrue(result.getAppliedDiscounts().containsKey("Extra 10% off on T-shirts"));
    }

    @Test
    void testCalculateCartDiscounts_super69() {
        TestData data = testDataConfig.super69VoucherData(); // Load 1st test data

        boolean result = discountService.validateDiscountCode(
                "SUPER69",
                data.getCartItems(),
                data.getCustomer()
        );

        assertTrue(result);
    }

    @Test
    void testCalculateCartDiscounts_customerTierCriteria() {
        TestData data = testDataConfig.customerTierGoldDiscountData(); // Load 1st test data

        DiscountedPrice result = discountService.calculateCartDiscounts(
                data.getCartItems(),
                data.getCustomer(),
                Optional.ofNullable(data.getPaymentInfo())
        );

        assertTrue(result.getAppliedDiscounts().containsKey("12% Off for Gold Members"));
    }
}