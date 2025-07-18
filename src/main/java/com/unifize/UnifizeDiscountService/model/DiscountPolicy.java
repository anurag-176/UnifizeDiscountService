package com.unifize.UnifizeDiscountService.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;
import java.io.Serializable;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@RedisHash("discount_policy")
public class DiscountPolicy implements Serializable {
    @Id
    private String id;
    private String name;

    private DiscountScope scope;                       // CART_ITEM, PRODUCT, etc.
    private int value;                                 // Discount value (e.g. 1000)
    private DiscountType type;                         // CURRENCY, PERCENTAGE

    private boolean isApplyToAll;                       // Apply to all matching items?
    private Integer applicationCount;                   // Limit on how many items to apply to

    private Integer maximumDiscountAmount;              // Cap for percentage discount

    @Indexed
    private String voucherCode;                         // Optional voucher trigger
    private DiscountTargetStrategyEnum targetStrategy;      // MIN_PRICE, FIRST_MATCH, etc.

    private String conditionExpression;                 // expression as a String
    private Integer stackOrder;                         // Lower number = higher priority

    @Indexed
    private Set<String> mutuallyExclusiveWith;
}
