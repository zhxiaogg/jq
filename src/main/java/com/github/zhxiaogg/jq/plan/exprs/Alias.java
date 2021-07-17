package com.github.zhxiaogg.jq.plan.exprs;

import com.github.zhxiaogg.jq.plan.exec.Record;
import com.github.zhxiaogg.jq.schema.DataType;
import com.github.zhxiaogg.jq.values.Value;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Data
@RequiredArgsConstructor
public class Alias implements NonLeafExprNode {
    private final Expression inner;
    private final String name;
    private final String id;

    public Alias(Expression inner, String name) {
        this(inner, name, UUID.randomUUID().toString());
    }

    @Override
    public Value eval(Record record) {
        return inner.eval(record);
    }

    @Override
    public String toString() {
        // TODO: fix this
        return name;
        // return String.format("%s_as_%s", inner, name);
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
        return new Alias(children.get(0), name, id);
    }
}
