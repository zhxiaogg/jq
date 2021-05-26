package com.github.zhxiaogg.jq.nodes.exprs;

import com.github.zhxiaogg.jq.nodes.plans.interpreter.Record;
import com.github.zhxiaogg.jq.schema.DataType;
import com.github.zhxiaogg.jq.values.Value;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Arrays;
import java.util.List;

@Data
@ToString
@EqualsAndHashCode
public class Alias implements Expression {
    private final Expression inner;
    private final String name;

    @Override
    public Value eval(Record record) {
        return inner.eval(record);
    }

    @Override
    public String getDisplayName() {
        return name;
    }

    @Override
    public DataType getDataType() {
        return inner.getDataType();
    }

    @Override
    public boolean leafNode() {
        return false;
    }

    @Override
    public List<Expression> getChildren() {
        return Arrays.asList(inner);
    }

    @Override
    public Expression withChildren(List<Expression> children) {
        return new Alias(children.get(0), name);
    }
}
