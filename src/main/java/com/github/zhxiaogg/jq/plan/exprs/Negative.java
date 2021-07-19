package com.github.zhxiaogg.jq.plan.exprs;

import com.github.zhxiaogg.jq.plan.exec.Record;
import com.github.zhxiaogg.jq.schema.DataType;
import com.github.zhxiaogg.jq.values.LiteralValue;
import com.github.zhxiaogg.jq.values.Value;
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
    public Value eval(Record record) {
        Value value = child.eval(record);
        switch (getDataType()) {
            case Int:
                return new LiteralValue(-(int) value.getValue(), DataType.Int);
            case Float:
                return new LiteralValue(-(double) value.getValue(), DataType.Float);
            default:
                throw new IllegalStateException("unsupported data type: " + getDataType());
        }
    }

    @Override
    public Object evaluate(Record record) {
        return eval(record).getValue();
    }

    @Override
    public DataType getDataType() {
        return child.getDataType();
    }
}
