package com.github.zhxiaogg.jq.plan.exprs.booleans;

import com.github.zhxiaogg.jq.datatypes.DataType;
import com.github.zhxiaogg.jq.plan.exec.Record;
import com.github.zhxiaogg.jq.plan.exprs.Expression;

public interface BooleanExpression extends Expression {
    @Override
    default DataType getDataType() {
        return DataType.Boolean;
    }

    @Override
    Boolean evaluate(Record record);
}
