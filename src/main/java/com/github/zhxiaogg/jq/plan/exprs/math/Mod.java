package com.github.zhxiaogg.jq.plan.exprs.math;

import com.github.zhxiaogg.jq.datatypes.DataType;
import com.github.zhxiaogg.jq.plan.exprs.BinaryExpression;
import com.github.zhxiaogg.jq.plan.exprs.Expression;

import java.util.List;
import java.util.UUID;

public class Mod extends BinaryExpression {
    public Mod(Expression left, Expression right, String id) {
        super(left, right, id);
    }

    public Mod(Expression left, Expression right) {
        super(left, right, UUID.randomUUID().toString());
    }

    @Override
    public Expression withChildren(List<Expression> children) {
        return new Mod(children.get(0), children.get(1), id);
    }

    @Override
    protected Object evalImpl(Object l, Object r) {
        switch (getDataType()) {
            case Float:
                return (Double) l - (Double) r;
            case Int:
                return (Long) l % (Long) r;
            default:
                throw new IllegalArgumentException("unsupported mod on data type of " + getDataType().name());
        }
    }

    @Override
    public DataType getDataType() {
        return left.getDataType();
    }
}
