package com.github.zhxiaogg.jq.plan.exprs.math;


import com.github.zhxiaogg.jq.plan.exprs.BinaryExpression;
import com.github.zhxiaogg.jq.plan.exprs.Expression;

import java.util.List;
import java.util.UUID;

public class Div extends BinaryExpression {

    public Div(Expression left, Expression right, String id) {
        super(left, right, id);
    }

    public Div(Expression left, Expression right) {
        this(left, right, UUID.randomUUID().toString());
    }

    @Override
    public Expression withChildren(List<Expression> children) {
        return new Div(children.get(0), children.get(1), id);
    }

    @Override
    protected Object evalImpl(Object l, Object r) {
        switch (getDataType()) {
            case Float:
                return (Double) l / (Double) r;
            case Int:
                return (Long) l / (Long) r;
            default:
                throw new IllegalArgumentException("unsupported div on data type of " + getDataType().name());
        }
    }
}
