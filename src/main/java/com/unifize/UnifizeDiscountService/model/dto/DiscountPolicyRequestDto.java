package com.unifize.UnifizeDiscountService.model.dto;

import com.unifize.UnifizeDiscountService.model.DiscountPolicy;
import com.unifize.UnifizeDiscountService.model.DiscountScope;
import com.unifize.UnifizeDiscountService.model.DiscountTargetStrategyEnum;
import com.unifize.UnifizeDiscountService.model.DiscountType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiscountPolicyRequestDto {

    @NotBlank
    private String name;

    @NotNull
    private DiscountScope scope;

    @NotNull
    private Integer value;

    @NotNull
    private DiscountType type;

    private String conditionExpression;

    private boolean isApplyToAll;

    private Integer applicationCount;

    private Integer maximumDiscountAmount;

    private String voucherCode;

    @NotNull
    private DiscountTargetStrategyEnum targetStrategy;

    @NotNull
    private Integer stackOrder;

    private Set<String> mutuallyExclusiveWith;

    public DiscountPolicy toEntity() {
        return DiscountPolicy.builder()
                .name(this.name)
                .scope(this.scope)
                .value(this.value)
                .type(this.type)
                .conditionExpression(this.conditionExpression)
                .isApplyToAll(this.isApplyToAll)
                .applicationCount(this.applicationCount)
                .maximumDiscountAmount(this.maximumDiscountAmount)
                .voucherCode(this.voucherCode)
                .targetStrategy(this.targetStrategy)
                .stackOrder(this.stackOrder)
                .mutuallyExclusiveWith(this.mutuallyExclusiveWith)
                .build();
    }
}
