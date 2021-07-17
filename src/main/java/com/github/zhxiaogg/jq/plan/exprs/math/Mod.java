package com.github.zhxiaogg.jq.plan.exprs.math;

import com.github.zhxiaogg.jq.plan.exprs.BinaryExpression;
import com.github.zhxiaogg.jq.plan.exprs.Expression;
import com.github.zhxiaogg.jq.schema.DataType;
import com.github.zhxiaogg.jq.values.Value;

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
    protected Value evalImpl(Value l, Value r) {
        return null;
    }

    @Override
    public DataType getDataType() {
        return left.getDataType();
    }
}
