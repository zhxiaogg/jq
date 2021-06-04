package com.github.zhxiaogg.jq.nodes.exprs.binary;

import com.github.zhxiaogg.jq.nodes.exprs.Expression;
import com.github.zhxiaogg.jq.nodes.logical.interpreter.Record;
import com.github.zhxiaogg.jq.schema.DataType;
import com.github.zhxiaogg.jq.values.Value;

import java.util.Arrays;
import java.util.List;

public abstract class BinaryExpression implements Expression {
    protected final Expression left;
    protected final Expression right;

    public BinaryExpression(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean leafNode() {
        return false;
    }

    @Override
    public List<Expression> getChildren() {
        return Arrays.asList(left, right);
    }

    @Override
    public Value eval(Record record) {
        Value l = left.eval(record);
        Value r = right.eval(record);
        return evalImpl(l, r);
    }

    abstract protected Value evalImpl(Value l, Value r);

    @Override
    public String getDisplayName() {
        return String.format("%s + %s", left.getDisplayName(), right.getDisplayName());
    }

    @Override
    public DataType getDataType() {
        if (left.getDataType() == right.getDataType()) {
            return left.getDataType();
        } else if (left.getDataType().canCastTo(right.getDataType())) {
            return right.getDataType();
        } else if (right.getDataType().canCastTo(left.getDataType())) {
            return left.getDataType();
        } else {
            throw new IllegalArgumentException("invalid children data types.");
        }
    }
}
