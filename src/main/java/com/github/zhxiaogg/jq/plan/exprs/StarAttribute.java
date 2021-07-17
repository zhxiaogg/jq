package com.github.zhxiaogg.jq.plan.exprs;

import com.github.zhxiaogg.jq.plan.exec.Record;
import com.github.zhxiaogg.jq.schema.DataType;
import com.github.zhxiaogg.jq.values.Value;
import lombok.Data;

import java.util.UUID;

@Data
public class StarAttribute implements LeafExprNode {
    private final String tableName;
    private final String id = UUID.randomUUID().toString();

    @Override
    public boolean isResolved() {
        return false;
    }

    @Override
    public Value eval(Record record) {
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
