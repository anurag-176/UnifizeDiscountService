package com.unifize.UnifizeDiscountService.model.engine;

public final class SimpleConditionNode implements ConditionNode {
    public final String lhs;
    public final Object rhs;
    public final ComparatorType operator;
    private final String expression;

    public SimpleConditionNode(String lhs, Object rhs, ComparatorType operator, String expression) {
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

