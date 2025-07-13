package com.unifize.UnifizeDiscountService.model.engine;

public sealed interface ConditionNode permits SimpleConditionNode, CompoundConditionNode {
    String getExpression();
}