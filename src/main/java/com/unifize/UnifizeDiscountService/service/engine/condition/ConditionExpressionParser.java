package com.unifize.UnifizeDiscountService.service.engine.condition;

import com.unifize.UnifizeDiscountService.exception.DiscountValidationException;
import com.unifize.UnifizeDiscountService.model.engine.ComparatorType;
import com.unifize.UnifizeDiscountService.model.engine.CompoundConditionNode;
import com.unifize.UnifizeDiscountService.model.engine.ConditionNode;
import com.unifize.UnifizeDiscountService.model.engine.SimpleConditionNode;
import java.util.*;
import java.util.regex.Pattern;

public class ConditionExpressionParser {
    private static final Set<String> OPERATORS = Set.of("==", ">=", "<=", "<", ">");
    private static final Set<String> LOGICAL_OPERATORS = Set.of("&&", "||");

    public static ConditionNode parse(String expr) throws DiscountValidationException {
        expr = expr.trim();

        if (!expr.contains("(")) {
            if (containsLogicalOperator(expr)) {
                // Force it to go down the compound logic path
                int opIndex = findTopLevelOperator(expr);
                if (opIndex == -1) {
                    throw new DiscountValidationException("Invalid logical expression: " + expr);
                }

                String operator = extractOperatorAt(expr, opIndex);
                String lhsExpr = expr.substring(0, opIndex).trim();
                String rhsExpr = expr.substring(opIndex + operator.length()).trim();

                ConditionNode lhsNode = parse(lhsExpr);
                ConditionNode rhsNode = parse(rhsExpr);
                return new CompoundConditionNode(lhsNode, rhsNode, toComparatorType(operator), expr);
            } else if (isValidSimpleCondition(expr)) {
                return parseSimpleCondition(expr);
            } else {
                throw new DiscountValidationException("Invalid simple condition: " + expr);
            }
        }

        if (!areBracketsBalanced(expr)) {
            throw new DiscountValidationException("Unbalanced brackets in: " + expr);
        }

        expr = unwrapOutermostBrackets(expr);

        int opIndex = findTopLevelOperator(expr);
        if (opIndex == -1) {
            if (isValidSimpleCondition(expr)) {
                return parseSimpleCondition(expr);
            } else {
                return parse(expr); // reparse deeper if compound
            }
        }

        String operator = extractOperatorAt(expr, opIndex);
        String lhsExpr = expr.substring(0, opIndex).trim();
        String rhsExpr = expr.substring(opIndex + operator.length()).trim();

        boolean lhsIsBracket = isWrappedInBrackets(lhsExpr);
        boolean rhsIsBracket = isWrappedInBrackets(rhsExpr);

        // 🤝 Instead of throwing error, treat bracketed sub-expression as compound or recurse
        ConditionNode lhsNode = parse(lhsIsBracket ? unwrapOutermostBrackets(lhsExpr) : lhsExpr);
        ConditionNode rhsNode = parse(rhsIsBracket ? unwrapOutermostBrackets(rhsExpr) : rhsExpr);

        return new CompoundConditionNode(lhsNode, rhsNode, toComparatorType(operator), expr);
    }

    private static boolean containsLogicalOperator(String expr) {
        return LOGICAL_OPERATORS.stream().anyMatch(expr::contains);
    }

    private static boolean isValidSimpleCondition(String expr) {
        boolean containsLogical = expr.contains("&&") || expr.contains("||");
        boolean hasOperator = OPERATORS.stream().anyMatch(expr::contains);
        boolean hasVar = expr.contains("$");
        return !containsLogical && hasOperator && hasVar;
    }

    private static SimpleConditionNode parseSimpleCondition(String expr) {
        for (String op : OPERATORS) {
            if (expr.contains(op)) {
                String[] parts = expr.split(Pattern.quote(op), 2);
                if (parts.length < 2) throw new DiscountValidationException("Malformed simple condition: " + expr);
                return new SimpleConditionNode(
                        parts[0].trim(),
                        stripQuotes(parts[1].trim()),
                        toComparatorType(op),
                        expr
                );
            }
        }
        throw new DiscountValidationException("No valid operator found in: " + expr);
    }

    private static ComparatorType toComparatorType(String op) {
        return switch (op) {
            case "==" -> ComparatorType.EQUALS;
            case ">" -> ComparatorType.GT;
            case ">=" -> ComparatorType.GTE;
            case "<" -> ComparatorType.LT;
            case "<=" -> ComparatorType.LTE;
            case "&&" -> ComparatorType.AND;
            case "||" -> ComparatorType.OR;
            default -> throw new DiscountValidationException("Unsupported operator: " + op);
        };
    }

    private static String extractOperatorAt(String expr, int index) {
        for (String op : LOGICAL_OPERATORS) {
            if (expr.startsWith(op, index)) return op;
        }
        throw new DiscountValidationException("No valid logical operator at index: " + index);
    }

    private static boolean areBracketsBalanced(String expr) {
        int count = 0;
        for (char ch : expr.toCharArray()) {
            if (ch == '(') count++;
            else if (ch == ')') count--;
            if (count < 0) return false;
        }
        return count == 0;
    }

    private static boolean isWrappedInBrackets(String expr) {
        return expr.startsWith("(") && expr.endsWith(")") && areBracketsBalanced(expr);
    }

    private static String unwrapOutermostBrackets(String expr) {
        while (isWrappedInBrackets(expr)) {
            expr = expr.substring(1, expr.length() - 1).trim();
        }
        return expr;
    }

    private static int findTopLevelOperator(String expr) {
        int depth = 0;
        for (int i = 0; i < expr.length(); i++) {
            char ch = expr.charAt(i);
            if (ch == '(') depth++;
            else if (ch == ')') depth--;
            else if (depth == 0) {
                for (String op : LOGICAL_OPERATORS) {
                    if (expr.startsWith(op, i)) return i;
                }
            }
        }
        return -1;
    }

    private static String stripQuotes(String s) {
        if ((s.startsWith("\"") && s.endsWith("\"")) || (s.startsWith("'") && s.endsWith("'"))) {
            return s.substring(1, s.length() - 1);
        }
        return s;
    }
}