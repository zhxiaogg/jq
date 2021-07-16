package com.github.zhxiaogg.jq.plan.exprs;

import com.github.zhxiaogg.jq.plan.exec.Record;
import com.github.zhxiaogg.jq.schema.DataType;
import com.github.zhxiaogg.jq.values.LiteralValue;
import com.github.zhxiaogg.jq.values.Value;

public interface BooleanExpression extends NonLeafExprNode {

    default Value eval(Record record) {
        return new LiteralValue(this.apply(record), DataType.Boolean);
    }

    boolean apply(Record record);

    @Override
    default boolean leafNode() {
        return false;
    }
}
