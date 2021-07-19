package com.github.zhxiaogg.jq.plan.exprs.literals;

import com.github.zhxiaogg.jq.plan.exec.Record;
import com.github.zhxiaogg.jq.plan.exprs.booleans.BooleanExpression;
import com.github.zhxiaogg.jq.schema.DataType;
import com.github.zhxiaogg.jq.values.Value;

import java.util.UUID;

public class BooleanLiteral implements Literal, BooleanExpression {
    private final boolean value;
    private final String id = UUID.randomUUID().toString();

    public BooleanLiteral(boolean value) {
        this.value = value;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean apply(Record record) {
        return value;
    }

    @Override
    public boolean isResolved() {
        return true;
    }

    @Override
    public Value eval(Record record) {
        return BooleanExpression.super.eval(record);
    }

    @Override
    public Boolean evaluate(Record record) {
        return value;
    }

    @Override
    public DataType getDataType() {
        return DataType.Boolean;
    }
}
