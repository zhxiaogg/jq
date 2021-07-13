package com.github.zhxiaogg.jq.plan.exprs.aggregators;

import com.github.zhxiaogg.jq.plan.exprs.Expression;
import com.github.zhxiaogg.jq.plan.logical.interpreter.Record;
import com.github.zhxiaogg.jq.values.LiteralValue;
import com.github.zhxiaogg.jq.values.Value;
import com.github.zhxiaogg.jq.values.agg.SumAggValue;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
public class Sum extends AggExpr {

    public Sum(Expression child) {
        super(child);
    }

    @Override
    public Value eval(Record record) {
        LiteralValue value = (LiteralValue) child.eval(record);
        return SumAggValue.from(value);
    }

    @Override
    public String toString() {
        return "Sum(" + child + ")";
    }

    @Override
    public Expression withChildren(List<Expression> children) {
        return new Sum(children.get(0));
    }
}
