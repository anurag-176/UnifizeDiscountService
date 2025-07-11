package com.unifize.UnifizeDiscountService.model.engine;

public class ConditionNode {

    /**
     * LHS can be:
     * - a string representing a variable (e.g., "$product.brand")
     * - another ConditionNode for compound conditions
     */
    public Object lhs;

    /**
     * RHS can be:
     * - a literal value (e.g., "PUMA", 1000)
     * - another ConditionNode for compound conditions
     */
    public Object rhs;

    /**
     * The operator (==, >, AND, OR, etc.)
     */
    public ComparatorType operator;

    public ConditionNode() {}

    public ConditionNode(Object lhs, Object rhs, ComparatorType operator) {
        this.lhs = lhs;
        this.rhs = rhs;
        this.operator = operator;
    }

    public boolean isCompound() {
        return lhs instanceof ConditionNode || rhs instanceof ConditionNode;
    }
}
