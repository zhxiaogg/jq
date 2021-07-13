package com.github.zhxiaogg.jq.plan.exprs;

import com.github.zhxiaogg.jq.plan.logical.interpreter.Record;
import com.github.zhxiaogg.jq.schema.DataType;
import com.github.zhxiaogg.jq.values.Value;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

@Data
public class Between implements Expression {
    private final Expression target;
    private final Expression left;
    private final Expression right;

    @Override
    public boolean leafNode() {
        return false;
    }

    @Override
    public List<Expression> getChildren() {
        return Arrays.asList(target, left, right);
    }

    @Override
    public Expression withChildren(List<Expression> children) {
        return new Between(children.get(0), children.get(1), children.get(2));
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
        return DataType.Boolean;
    }
}
