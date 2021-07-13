package com.github.zhxiaogg.jq.plan.exprs;

import com.github.zhxiaogg.jq.plan.logical.interpreter.Record;
import com.github.zhxiaogg.jq.schema.DataType;
import com.github.zhxiaogg.jq.values.Value;
import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class Negative implements Expression {
    private final Expression child;

    @Override
    public boolean leafNode() {
        return false;
    }

    @Override
    public List<Expression> getChildren() {
        return Collections.singletonList(child);
    }

    @Override
    public Expression withChildren(List<Expression> children) {
        return new Negative(children.get(0));
    }

    @Override
    public Value eval(Record record) {
        return null;
    }

    @Override
    public String toString() {
        return null;
    }

    @Override
    public DataType getDataType() {
        return null;
    }
}
