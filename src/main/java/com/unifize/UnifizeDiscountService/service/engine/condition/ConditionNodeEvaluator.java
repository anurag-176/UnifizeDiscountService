package com.unifize.UnifizeDiscountService.service.engine.condition;

import com.unifize.UnifizeDiscountService.model.engine.ComparatorType;
import com.unifize.UnifizeDiscountService.model.engine.ConditionNode;
import java.lang.reflect.Field;
import java.util.Map;

public class ConditionNodeEvaluator {

    public static boolean evaluate(ConditionNode node, Map<String, Object> context) {
        if (node == null || node.operator == null) return true;

        // Evaluate compound subtrees recursively
        if (node.isCompound()) {
            boolean left = evaluate((ConditionNode) node.lhs, context);
            boolean right = evaluate((ConditionNode) node.rhs, context);
            return switch (node.operator) {
                case AND -> left && right;
                case OR -> left || right;
                default -> throw new RuntimeException("Invalid compound operator: " + node.operator);
            };
        }

        // Resolve LHS and RHS values
        Object lhsValue = resolveOperand(node.lhs, context);
        Object rhsValue = node.rhs;

        return compare(lhsValue, rhsValue, node.operator);
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
