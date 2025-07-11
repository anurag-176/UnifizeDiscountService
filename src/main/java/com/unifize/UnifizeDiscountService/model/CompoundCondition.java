package com.unifize.UnifizeDiscountService.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompoundCondition implements DiscountConditionNode {
    private LogicalOperator operator;                    // AND or OR
    private List<DiscountConditionNode> children;        // Nested conditions
}