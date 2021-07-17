package com.github.zhxiaogg.jq.plan.exprs;

import com.github.zhxiaogg.jq.plan.exprs.aggregators.SumAgg;
import com.github.zhxiaogg.jq.plan.exprs.booleans.BooleanExpression;
import com.github.zhxiaogg.jq.plan.exprs.booleans.Compare;
import com.github.zhxiaogg.jq.plan.exprs.booleans.CompareOp;
import com.github.zhxiaogg.jq.plan.exprs.literals.Literal;

import java.time.Instant;

@Deprecated
public class Expressions {
    public static SumAgg sum(String attribute) {
        return new SumAgg(new UnResolvedAttribute(null, attribute));
    }

    public static Expression attri(String attribute) {
        return new UnResolvedAttribute(null, attribute);
    }

    public static BooleanExpression gt(Expression left, Expression right) {
        return new Compare(CompareOp.GT, left, right);
    }

    public static BooleanExpression gt(Expression left, int right) {
        return new Compare(CompareOp.GT, left, Literal.create(right));
    }


    public static BooleanExpression gt(String left, Instant right) {
        return new Compare(CompareOp.GT, new UnResolvedAttribute(null, left), Literal.create(right));
    }

    public static Expression alias(Expression inner, String name) {
        return new Alias(inner, name);
    }

    public static BooleanExpression gte(Expression left, int right) {
        return new Compare(CompareOp.GTE, left, Literal.create(right));
    }
}
