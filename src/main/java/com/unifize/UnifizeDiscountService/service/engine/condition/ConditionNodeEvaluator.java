package com.unifize.UnifizeDiscountService.service.engine.condition;

import com.unifize.UnifizeDiscountService.exception.DiscountCalculationException;
import com.unifize.UnifizeDiscountService.model.engine.ComparatorType;
import com.unifize.UnifizeDiscountService.model.engine.CompoundConditionNode;
import com.unifize.UnifizeDiscountService.model.engine.ConditionNode;
import com.unifize.UnifizeDiscountService.model.engine.SimpleConditionNode;
import java.util.Map;

public class ConditionNodeEvaluator {

    public static boolean evaluate(ConditionNode node, Map<String, Object> context) {
        if (node == null) return true;

        if (node instanceof CompoundConditionNode compound) {
            boolean left = evaluate(compound.lhs, context);
            boolean right = evaluate(compound.rhs, context);
            return switch (compound.operator) {
                case AND -> left && right;
                case OR -> left || right;
                default -> throw new DiscountCalculationException("Invalid compound operator: " + compound.operator);
            };
        }

        if (node instanceof SimpleConditionNode simple) {
            Object lhsValue = resolveOperand(simple.lhs, context);
            Object rhsValue = simple.rhs;
            return compare(lhsValue, rhsValue, simple.operator);
        }

        throw new DiscountCalculationException("Unknown node type: " + node.getClass());
    }

    private static Object resolveOperand(Object operand, Map<String, Object> context) {
        if (!(operand instanceof String path)) return operand;
        if (!path.startsWith("$")) return operand;

        String[] parts = path.substring(1).split("\\.");
        Object current = context.get(parts[0]);

        for (int i = 1; i < parts.length && current != null; i++) {
            if (!(current instanceof Map<?, ?>)) {
                return null;
            }
            current = ((Map<?, ?>) current).get(parts[i]);
        }
        return current;
    }

    private static boolean compare(Object lhs, Object rhs, ComparatorType op) {
        if (lhs == null || rhs == null) return false;

        try {
            if (lhs instanceof Number && rhs instanceof Number) {
                double left = ((Number) lhs).doubleValue();
                double right = ((Number) rhs).doubleValue();

                return switch (op) {
                    case EQUALS -> left == right;
                    case GT -> left > right;
                    case GTE -> left >= right;
                    case LT -> left < right;
                    case LTE -> left <= right;
                    default -> false;
                };
            } else {
                String leftStr = lhs.toString();
                String rightStr = rhs.toString();

                return switch (op) {
                    case EQUALS -> leftStr.equals(rightStr);
                    case GT -> leftStr.compareTo(rightStr) > 0;
                    case GTE -> leftStr.compareTo(rightStr) >= 0;
                    case LT -> leftStr.compareTo(rightStr) < 0;
                    case LTE -> leftStr.compareTo(rightStr) <= 0;
                    default -> false;
                };
            }
        } catch (Exception e) {
            return false;
        }
    }
}