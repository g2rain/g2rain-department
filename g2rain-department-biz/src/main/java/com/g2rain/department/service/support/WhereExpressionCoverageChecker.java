package com.g2rain.department.service.support;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.LikeExpression;
import net.sf.jsqlparser.expression.operators.relational.ParenthesedExpressionList;
import net.sf.jsqlparser.schema.Column;

import java.util.Locale;
import java.util.Objects;

/**
 * 判断用户 WHERE 是否已蕴含要求的隔离条件。
 */
public final class WhereExpressionCoverageChecker {

    private WhereExpressionCoverageChecker() {
    }

    public static boolean covers(Expression userWhere, Expression requiredCondition) {
        if (Objects.isNull(requiredCondition)) {
            return true;
        }
        if (Objects.isNull(userWhere)) {
            return false;
        }
        return implies(userWhere, requiredCondition);
    }

    private static boolean implies(Expression user, Expression required) {
        if (required instanceof ParenthesedExpressionList<?> parenthesed) {
            if (parenthesed.isEmpty()) {
                return true;
            }
            return implies(user, parenthesed.getFirst());
        }
        if (required instanceof AndExpression and) {
            return implies(user, and.getLeftExpression()) && implies(user, and.getRightExpression());
        }
        if (required instanceof OrExpression or) {
            return implies(user, or.getLeftExpression()) || implies(user, or.getRightExpression());
        }
        return matchesLeaf(user, required);
    }

    private static boolean matchesLeaf(Expression user, Expression requiredLeaf) {
        if (expressionEquivalent(user, requiredLeaf)) {
            return true;
        }
        if (user instanceof AndExpression and) {
            return matchesLeaf(and.getLeftExpression(), requiredLeaf)
                || matchesLeaf(and.getRightExpression(), requiredLeaf);
        }
        if (user instanceof OrExpression or) {
            return matchesLeaf(or.getLeftExpression(), requiredLeaf)
                || matchesLeaf(or.getRightExpression(), requiredLeaf);
        }
        if (user instanceof ParenthesedExpressionList<?> parenthesed && !parenthesed.isEmpty()) {
            return matchesLeaf(parenthesed.getFirst(), requiredLeaf);
        }
        return false;
    }

    private static boolean expressionEquivalent(Expression left, Expression right) {
        if (left instanceof EqualsTo leftEq && right instanceof EqualsTo rightEq) {
            return columnRefEquals(leftEq.getLeftExpression(), rightEq.getLeftExpression())
                && valueEquals(leftEq.getRightExpression(), rightEq.getRightExpression());
        }
        if (left instanceof LikeExpression leftLike && right instanceof LikeExpression rightLike) {
            return columnRefEquals(leftLike.getLeftExpression(), rightLike.getLeftExpression())
                && valueEquals(leftLike.getRightExpression(), rightLike.getRightExpression());
        }
        return normalizeExpression(left).equals(normalizeExpression(right));
    }

    private static boolean columnRefEquals(Expression left, Expression right) {
        return normalizeColumn(left).equals(normalizeColumn(right));
    }

    private static String normalizeColumn(Expression expression) {
        if (expression instanceof Column column) {
            return column.getFullyQualifiedName().replace("`", "").toLowerCase(Locale.ROOT);
        }
        return normalizeExpression(expression);
    }

    private static boolean valueEquals(Expression left, Expression right) {
        return normalizeExpression(left).equals(normalizeExpression(right));
    }

    private static String normalizeExpression(Expression expression) {
        if (Objects.isNull(expression)) {
            return "";
        }
        if (expression instanceof LongValue longValue) {
            return String.valueOf(longValue.getValue());
        }
        if (expression instanceof StringValue stringValue) {
            return stringValue.getValue();
        }
        return expression.toString().replace(" ", "").replace("`", "").toLowerCase(Locale.ROOT);
    }
}
