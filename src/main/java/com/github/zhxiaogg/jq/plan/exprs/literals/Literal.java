package com.github.zhxiaogg.jq.plan.exprs.literals;

import com.github.zhxiaogg.jq.datatypes.DataType;
import com.github.zhxiaogg.jq.plan.exec.Record;
import com.github.zhxiaogg.jq.plan.exprs.Expression;
import com.github.zhxiaogg.jq.plan.exprs.LeafExprNode;

import java.time.Instant;

public interface Literal extends LeafExprNode {

    Object getValue();

    static Expression create(int value) {
        return new LiteralImpl(value, DataType.Int);
    }

    static Expression create(Instant value) {
        return new LiteralImpl(value, DataType.DateTime);
    }

    @Override
    default boolean isResolved() {
        return true;
    }

    @Override
    default Object evaluate(Record record) {
        return getValue();
    }
}
