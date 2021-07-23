package com.github.zhxiaogg.jq.plan.exprs;

import com.github.zhxiaogg.jq.plan.exec.Record;
import com.github.zhxiaogg.jq.schema.DataType;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Data
@RequiredArgsConstructor
public class Negative implements NonLeafExprNode {
    private final Expression child;
    private final String id;

    public Negative(Expression child) {
        this(child, UUID.randomUUID().toString());
    }

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
        return new Negative(children.get(0), this.id);
    }

    @Override
    public boolean semanticEqual(Expression other) {
        return other instanceof Negative &&
                child.semanticEqual(((Negative) other).getChild());
    }

    @Override
    public Object evaluate(Record record) {
        Object value = child.evaluate(record);
        switch (getDataType()) {
            case Int:
                return -(int) value;
            case Float:
                return -(double) value;
            default:
                throw new IllegalStateException("unsupported data type: " + getDataType());
        }
    }

    @Override
    public DataType getDataType() {
        return child.getDataType();
    }
}
