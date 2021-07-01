package com.github.zhxiaogg.jq.plan.exprs;

import com.github.zhxiaogg.jq.plan.logical.interpreter.Record;
import com.github.zhxiaogg.jq.schema.DataType;
import com.github.zhxiaogg.jq.values.Value;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode
public class UnResolvedAttribute implements LeafExprNode {
    private final String name;

    public UnResolvedAttribute(String name) {
        this.name = name;
    }

    @Override
    public Value eval(Record record) {
        throw new UnsupportedOperationException("");
    }

    @Override
    public String getDisplayName() {
        return name;
    }

    @Override
    public DataType getDataType() {
        return DataType.UnKnown;
    }
}
