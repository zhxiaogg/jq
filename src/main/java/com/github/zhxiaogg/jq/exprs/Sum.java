package com.github.zhxiaogg.jq.exprs;

import com.github.zhxiaogg.jq.plans.interpreter.Record;
import com.github.zhxiaogg.jq.values.LiteralValue;
import com.github.zhxiaogg.jq.values.SumAgg;
import com.github.zhxiaogg.jq.values.Value;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode
public class Sum implements Expression {
    private final Expression expression;

    public Sum(Expression expression) {
        this.expression = expression;
    }

    @Override
    public Value eval(Record record) {
        LiteralValue value = (LiteralValue) expression.eval(record);
        return  SumAgg.from(value);
    }
}
