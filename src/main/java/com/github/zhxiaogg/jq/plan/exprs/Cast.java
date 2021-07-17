package com.github.zhxiaogg.jq.plan.exprs;

import com.github.zhxiaogg.jq.plan.exec.Record;
import com.github.zhxiaogg.jq.schema.DataType;
import com.github.zhxiaogg.jq.values.LiteralValue;
import com.github.zhxiaogg.jq.values.Value;
import lombok.Data;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.github.zhxiaogg.jq.utils.Requires.require;

@Data
public class Cast implements NonLeafExprNode {
    private final Expression child;
    private final DataType dataType;
    private final String id;

    public Cast(Expression child, DataType dataType, String id) {
        require(child.getDataType().canCastTo(dataType), "expression cannot be cast to specific data type!");
        this.child = child;
        this.dataType = dataType;
        this.id = id;
    }

    public Cast(Expression child, DataType dataType) {
        this(child, dataType, UUID.randomUUID().toString());
    }

    @Override
    public List<Expression> getChildren() {
        return Collections.singletonList(child);
    }

    @Override
    public Expression withChildren(List<Expression> children) {
        return new Cast(children.get(0), dataType, id);
    }

    @Override
    public Value eval(Record record) {
        Value value = child.eval(record);
        if (value instanceof LiteralValue) {
            return new LiteralValue(value.getDataType().castTo(dataType, value.getValue()), dataType);
        } else {
            throw new IllegalStateException("unsupported cast!");
        }
    }
}
