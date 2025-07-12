package com.unifize.UnifizeDiscountService.model.dto;

import com.unifize.UnifizeDiscountService.model.DiscountPolicy;
import com.unifize.UnifizeDiscountService.model.DiscountScope;
import com.unifize.UnifizeDiscountService.model.DiscountTargetStrategyEnum;
import com.unifize.UnifizeDiscountService.model.DiscountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiscountPolicyResponseDto {

    private String id;
    private String name;
    private DiscountScope scope;
    private Integer value;
    private DiscountType type;
    private String conditionExpression;
    private boolean isApplyToAll;
    private Integer applicationCount;
    private Integer maximumDiscountAmount;
    private String voucherCode;
    private DiscountTargetStrategyEnum targetStrategy;
    private Integer stackOrder;
    private Set<String> mutuallyExclusiveWith;

    public static DiscountPolicyResponseDto from(DiscountPolicy policy) {
        return DiscountPolicyResponseDto.builder()
                .id(policy.getId())
                .name(policy.getName())
                .scope(policy.getScope())
                .value(policy.getValue())
                .type(policy.getType())
                .conditionExpression(policy.getConditionExpression())
                .isApplyToAll(policy.isApplyToAll())
                .applicationCount(policy.getApplicationCount())
                .maximumDiscountAmount(policy.getMaximumDiscountAmount())
                .voucherCode(policy.getVoucherCode())
                .targetStrategy(policy.getTargetStrategy())
                .stackOrder(policy.getStackOrder())
                .mutuallyExclusiveWith(policy.getMutuallyExclusiveWith())
                .build();
    }
}
