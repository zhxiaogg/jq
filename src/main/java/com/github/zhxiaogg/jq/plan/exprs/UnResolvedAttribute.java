package com.github.zhxiaogg.jq.plan.exprs;

import com.github.zhxiaogg.jq.datatypes.DataType;
import com.github.zhxiaogg.jq.plan.exec.AttributeSet;
import com.github.zhxiaogg.jq.plan.exec.Record;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.UUID;

@Data
@RequiredArgsConstructor
public class UnResolvedAttribute implements LeafExprNode {
    private final String[] names;
    private final String id;

    public UnResolvedAttribute(String[] names) {
        this(names, UUID.randomUUID().toString());
    }

    @Override
    public boolean semanticEqual(Expression other) {
        return other instanceof UnResolvedAttribute &&
                Arrays.equals(names, ((UnResolvedAttribute) other).getNames());
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
        return String.join(".", names);
    }

    @Override
    public DataType getDataType() {
        return DataType.UnKnown;
    }

    @Override
    public ResolvedAttribute toAttribute() {
        return new ResolvedAttribute(id, names, getDataType(), new int[0], AttributeSet.empty(new String[0]));
    }
}
