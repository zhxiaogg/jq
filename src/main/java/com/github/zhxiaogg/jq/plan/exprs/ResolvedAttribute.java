package com.github.zhxiaogg.jq.plan.exprs;

import com.github.zhxiaogg.jq.plan.exec.Record;
import com.github.zhxiaogg.jq.schema.DataType;
import com.github.zhxiaogg.jq.values.Value;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Data
@RequiredArgsConstructor
public class ResolvedAttribute implements LeafExprNode {
    private final String id;
    private final String name;
    private final DataType dataType;
    @Deprecated
    private final int ordinal;

    public ResolvedAttribute(String name, DataType dataType, int ordinal) {
        this(UUID.randomUUID().toString(), name, dataType, ordinal);
    }

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
