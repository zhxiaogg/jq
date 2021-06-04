package com.github.zhxiaogg.jq.nodes.exprs;

import com.github.zhxiaogg.jq.nodes.logical.interpreter.Record;
import com.github.zhxiaogg.jq.schema.DataType;
import com.github.zhxiaogg.jq.values.LiteralValue;
import com.github.zhxiaogg.jq.values.Value;

public interface BooleanExpression extends Expression {

    default Value eval(Record record) {
        return new LiteralValue(this.apply(record), DataType.Boolean);
    }

    boolean apply(Record record);

    @Override
    default boolean leafNode() {
        return false;
    }
}
