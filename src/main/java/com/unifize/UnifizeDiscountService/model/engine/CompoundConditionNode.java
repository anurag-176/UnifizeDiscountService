package com.unifize.UnifizeDiscountService.model.engine;

public final class CompoundConditionNode implements ConditionNode {
    public final ConditionNode lhs;
    public final ConditionNode rhs;
    public final ComparatorType operator;
    private final String expression;

    public CompoundConditionNode(ConditionNode lhs, ConditionNode rhs, ComparatorType operator, String expression) {
        this.lhs = lhs;
        this.rhs = rhs;
        this.operator = operator;
        this.expression = expression;
    }

    @Override
    public String getExpression() {
        return expression;
    }
}

