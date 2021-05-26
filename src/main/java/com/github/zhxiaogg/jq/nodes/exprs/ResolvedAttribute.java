package com.github.zhxiaogg.jq.nodes.exprs;

import com.github.zhxiaogg.jq.nodes.plans.interpreter.Record;
import com.github.zhxiaogg.jq.schema.DataType;
import com.github.zhxiaogg.jq.values.LiteralValue;
import com.github.zhxiaogg.jq.values.Value;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Collections;
import java.util.List;

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
