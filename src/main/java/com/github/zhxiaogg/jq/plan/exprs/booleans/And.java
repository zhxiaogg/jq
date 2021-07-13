package com.github.zhxiaogg.jq.plan.exprs.booleans;

import com.github.zhxiaogg.jq.plan.exprs.BinaryExpression;
import com.github.zhxiaogg.jq.plan.exprs.Expression;
import com.github.zhxiaogg.jq.values.Value;

import java.util.List;

public class And extends BinaryExpression {
    public And(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public Expression withChildren(List<Expression> children) {
        return new And(children.get(0), children.get(1));
    }

    @Override
    protected Value evalImpl(Value l, Value r) {
        return null;
    }

    @Override
    public String toString() {
        return String.format("%s AND %s", left, right);
    }
}
