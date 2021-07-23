package com.github.zhxiaogg.jq.plan.exprs.literals;

import com.github.zhxiaogg.jq.datatypes.DataType;
import com.github.zhxiaogg.jq.plan.exec.Record;
import com.github.zhxiaogg.jq.plan.exprs.Expression;
import com.github.zhxiaogg.jq.plan.exprs.booleans.BooleanExpression;

import java.util.Objects;
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
    public boolean semanticEqual(Expression other) {
        return other instanceof BooleanLiteral &&
                Objects.equals(value, ((BooleanLiteral) other).value);
    }

    @Override
    public boolean isResolved() {
        return true;
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
