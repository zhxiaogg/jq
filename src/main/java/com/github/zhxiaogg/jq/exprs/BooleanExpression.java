package com.github.zhxiaogg.jq.exprs;

import com.github.zhxiaogg.jq.plans.interpreter.Record;
import com.github.zhxiaogg.jq.schema.DataType;
import com.github.zhxiaogg.jq.values.LiteralValue;
import com.github.zhxiaogg.jq.values.Value;

public interface BooleanExpression extends Expression {

    @Override
    public default Value eval(Record record) {
        return new LiteralValue(this.apply(record), DataType.Boolean);
    }

    boolean apply(Record record);

}
