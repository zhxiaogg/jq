package com.github.zhxiaogg.jq.exprs;

import java.time.Instant;

public class Expressions {
    public static Sum sum(String attribute) {
        return new Sum(new UnResolvedAttribute(attribute));
    }

    public static BooleanExpression gt(Expression left, Expression right) {
        return new Compare(CompareOp.GT, left, right);
    }

    public static BooleanExpression gt(Expression left, int right) {
        return new Compare(CompareOp.GT, left, Literal.create(right));
    }


    public static BooleanExpression gt(String left, Instant right) {
        return new Compare(CompareOp.GT, new UnResolvedAttribute(left), Literal.create(right));
    }

    public static Expression alias(Expression inner, String name) {
        return new Alias(inner, name);
    }

    public static BooleanExpression gte(Expression left, int right) {
        return new Compare(CompareOp.GTE, left, Literal.create(right));
    }
}
