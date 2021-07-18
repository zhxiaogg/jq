package com.github.zhxiaogg.jq.plan.exprs;

import com.github.zhxiaogg.jq.plan.exec.Record;
import com.github.zhxiaogg.jq.schema.DataType;
import com.github.zhxiaogg.jq.values.Value;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Data
@RequiredArgsConstructor
public class UnResolvedAttribute implements LeafExprNode {
    private final String tableName;
    private final String name;
    private final String id;

    public UnResolvedAttribute(String tableName, String name) {
        this(tableName, name, UUID.randomUUID().toString());
    }

    @Override
    public boolean isResolved() {
        return false;
    }

    @Override
    public Value eval(Record record) {
        throw new UnsupportedOperationException("");
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public DataType getDataType() {
        return DataType.UnKnown;
    }
}
