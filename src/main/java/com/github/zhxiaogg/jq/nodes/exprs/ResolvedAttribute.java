package com.github.zhxiaogg.jq.nodes.exprs;

import com.github.zhxiaogg.jq.nodes.logical.interpreter.Record;
import com.github.zhxiaogg.jq.schema.DataType;
import com.github.zhxiaogg.jq.values.Value;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode
public class ResolvedAttribute implements LeafExprNode {
    private final String name;
    private final DataType dataType;
    private final int index;

    public static ResolvedAttribute create(String name, DataType dataType, int index) {
        return new ResolvedAttribute(name, dataType, index);
    }

    @Override
    public Value eval(Record record) {
        return record.getValues().get(index);
    }

    @Override
    public String getDisplayName() {
        return name;
    }
}
