package com.github.zhxiaogg.jq.plan.exprs;

import com.github.zhxiaogg.jq.datatypes.DataType;
import com.github.zhxiaogg.jq.plan.exec.Record;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

@Data
@RequiredArgsConstructor
public class ResolvedAttribute implements LeafExprNode {
    private final String id;
    private final String[] names;
    private final DataType dataType;
    private final int ordinal;

    public ResolvedAttribute(String name, DataType dataType, int ordinal) {
        this(UUID.randomUUID().toString(), new String[]{name}, dataType, ordinal);
    }

    @Override
    public boolean semanticEqual(Expression other) {
        return other instanceof ResolvedAttribute &&
                Objects.equals(id, other.getId()) &&
                Arrays.equals(names, ((ResolvedAttribute) other).getNames());
    }

    @Override
    public boolean isResolved() {
        return true;
    }

    @Override
    public Object evaluate(Record record) {
        return record.indexOf(ordinal);
    }

    public ResolvedAttribute withOrdinal(int value) {
        if (value == this.ordinal) return this;
        return new ResolvedAttribute(id, names, dataType, value);
    }

    @Override
    public String toString() {
        return String.join(".", names);
    }
}
