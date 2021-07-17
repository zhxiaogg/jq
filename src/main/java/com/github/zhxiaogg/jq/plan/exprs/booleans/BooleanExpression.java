package com.github.zhxiaogg.jq.plan.exprs.booleans;

import com.github.zhxiaogg.jq.plan.exec.Record;
import com.github.zhxiaogg.jq.plan.exprs.Expression;
import com.github.zhxiaogg.jq.schema.DataType;
import com.github.zhxiaogg.jq.values.LiteralValue;
import com.github.zhxiaogg.jq.values.Value;

public interface BooleanExpression extends Expression {
    @Override
    default DataType getDataType() {
        return DataType.Boolean;
    }

    @Override
    default Value eval(Record record) {
        return new LiteralValue(this.apply(record), DataType.Boolean);
    }

    boolean apply(Record record);
}
