package com.unifize.UnifizeDiscountService.service.engine.condition;

import com.unifize.UnifizeDiscountService.model.engine.ComparatorType;
import com.unifize.UnifizeDiscountService.model.engine.ConditionNode;
import java.util.*;
import java.util.regex.Pattern;

public class ConditionExpressionParser {

    private static final Set<String> OPERATORS = Set.of("==", ">=", "<=", "<", ">");

    /**
     * Parses a basic condition like "$product.brand == 'PUMA'" or compound using AND/OR
     */
    public static ConditionNode parse(String expression) {
        expression = expression.trim();

        // Check for compound (OR / AND)
        if (expression.contains("||")) {
            return buildLogical(expression, "||", ComparatorType.EQUALS); // use EQUALS to represent OR logically
        } else if (expression.contains("&&")) {
            return buildLogical(expression, "&&", ComparatorType.EQUALS); // use EQUALS to represent AND logically
        } else {
            return parseSimpleCondition(expression);
        }
    }

    private static ConditionNode buildLogical(String expr, String operator, ComparatorType type) {
        String[] parts = expr.split(Pattern.quote(operator), 2);
        return new ConditionNode(
                parse(parts[0].trim()),
                parse(parts[1].trim()),
                operator.equals("&&") ? ComparatorType.AND : ComparatorType.OR
        );
    }

    private static ConditionNode parseSimpleCondition(String expression) {
        for (String op : OPERATORS) {
            if (expression.contains(op)) {
                String[] parts = expression.split(Pattern.quote(op), 2);
                return new ConditionNode(
                        parts[0].trim(),
                        stripQuotes(parts[1].trim()),
                        switch (op) {
                            case "==" -> ComparatorType.EQUALS;
                            case ">" -> ComparatorType.GT;
                            case ">=" -> ComparatorType.GTE;
                            case "<" -> ComparatorType.LT;
                            case "<=" -> ComparatorType.LTE;
                            default -> throw new IllegalArgumentException("Unsupported operator: " + op);
                        }
                );
            }
        }
        throw new IllegalArgumentException("Invalid condition expression: " + expression);
    }

    private static String stripQuotes(String s) {
        return s.replaceAll("^['\"]|['\"]$", "");
    }
}
