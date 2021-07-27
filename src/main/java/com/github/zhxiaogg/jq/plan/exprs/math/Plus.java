package com.github.zhxiaogg.jq.plan.exprs.math;

import com.github.zhxiaogg.jq.plan.exprs.BinaryExpression;
import com.github.zhxiaogg.jq.plan.exprs.Expression;

import java.util.List;
import java.util.UUID;

public class Plus extends BinaryExpression {
    public Plus(Expression left, Expression right, String id) {
        super(left, right, id);
    }

    public Plus(Expression left, Expression right) {
        super(left, right, UUID.randomUUID().toString());
    }

    @Override
    public Expression withChildren(List<Expression> children) {
        return new Plus(children.get(0), children.get(1), id);
    }

    @Override
    public Object evalImpl(Object l, Object r) {
        switch (getDataType()) {
            case Float:
                return (Double) l + (Double) r;
            case Int:
                return (Long) l + (Long) r;
            case String:
                return (String) l + (String) r;
            case Any:
                // TODO: improve this?
                return ((Number) l).doubleValue() + ((Number) r).doubleValue();
            default:
                throw new IllegalArgumentException("unsupported plus on data type of " + getDataType().name());
        }
    }
}
