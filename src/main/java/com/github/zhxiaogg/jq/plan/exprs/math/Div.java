package com.github.zhxiaogg.jq.plan.exprs.math;


import com.github.zhxiaogg.jq.plan.exprs.BinaryExpression;
import com.github.zhxiaogg.jq.plan.exprs.Expression;
import com.github.zhxiaogg.jq.schema.DataType;
import com.github.zhxiaogg.jq.utils.BinaryValueOp;
import com.github.zhxiaogg.jq.values.AggValue;
import com.github.zhxiaogg.jq.values.LiteralValue;
import com.github.zhxiaogg.jq.values.Value;
import com.github.zhxiaogg.jq.values.agg.binary.DivAggValue;

import java.util.List;
import java.util.UUID;

public class Div extends BinaryExpression implements BinaryValueOp<Value> {

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
    protected Value evalImpl(Value l, Value r) {
        if (l instanceof AggValue && r instanceof AggValue) {
            return new DivAggValue((AggValue) l, (AggValue) r);
        } else if (l instanceof LiteralValue && r instanceof LiteralValue) {
            return this.apply(l, r);
        } else {
            throw new IllegalArgumentException("not supported for now!");
        }
    }

    @Override
    public Value applyWithDataType(DataType dataType, Object l, Object r) {
        switch (dataType) {
            case Float:
                return new LiteralValue((Double) l / (Double) r, dataType);
            case Int:
                return new LiteralValue((Long) l / (Long) r, dataType);
            default:
                throw new IllegalArgumentException("unsupported div on data type of " + dataType.name());
        }
    }
}