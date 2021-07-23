package com.github.zhxiaogg.jq.plan.exprs;

import com.github.zhxiaogg.jq.plan.exec.Record;
import com.github.zhxiaogg.jq.schema.DataType;
import lombok.Data;

import java.util.Objects;
import java.util.UUID;

@Data
public class StarAttribute implements LeafExprNode {
    private final String tableName;
    private final String id = UUID.randomUUID().toString();

    @Override
    public boolean semanticEqual(Expression other) {
        return other instanceof StarAttribute &&
                Objects.equals(tableName, ((StarAttribute) other).tableName);
    }

    @Override
    public boolean isResolved() {
        return false;
    }

    @Override
    public Object evaluate(Record record) {
        return null;
    }

    @Override
    public String toString() {
        return "*";
    }

    @Override
    public DataType getDataType() {
        return DataType.UnKnown;
    }
}
