package com.github.zhxiaogg.jq.plan.exprs.booleans;

import com.github.zhxiaogg.jq.plan.exec.Record;
import com.github.zhxiaogg.jq.plan.exprs.Expression;
import com.github.zhxiaogg.jq.schema.DataType;

public interface BooleanExpression extends Expression {
    @Override
    default DataType getDataType() {
        return DataType.Boolean;
    }

    @Override
    Boolean evaluate(Record record);
}
