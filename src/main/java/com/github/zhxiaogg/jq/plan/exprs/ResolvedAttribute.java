package com.github.zhxiaogg.jq.plan.exprs;

import com.github.zhxiaogg.jq.plan.exec.Record;
import com.github.zhxiaogg.jq.schema.DataType;
import com.github.zhxiaogg.jq.values.Value;
import lombok.Data;

@Data
public class ResolvedAttribute implements LeafExprNode {
    private final String id;
    private final String name;
    private final DataType dataType;
    private final int ordinal;

    @Override
    public boolean isResolved() {
        return true;
    }

    @Override
    public Value eval(Record record) {
        return record.getValues().get(ordinal);
    }

    @Override
    public String toString() {
        return name;
    }
}
