package com.github.zhxiaogg.jq.plan.exprs;

import com.github.zhxiaogg.jq.plan.exec.Record;
import com.github.zhxiaogg.jq.schema.DataType;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
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
    public boolean semanticEqual(Expression other) {
        return other instanceof Alias &&
                Objects.equals(this.name, ((Alias) other).name) &&
                inner.semanticEqual(((Alias) other).getInner());
    }

    @Override
    public Object evaluate(Record record) {
        return inner.evaluate(record);
    }

    @Override
    public String toString() {
        return String.format("%s AS %s", inner, name);
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
