package com.github.zhxiaogg.jq.plan.exprs;

import com.github.zhxiaogg.jq.plan.logical.interpreter.Record;
import com.github.zhxiaogg.jq.schema.DataType;
import com.github.zhxiaogg.jq.values.Value;
import lombok.Data;

@Data
public class StarAttribute implements LeafExprNode {
    private final String tableName;

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
        return null;
    }
}
