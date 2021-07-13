package com.github.zhxiaogg.jq.plan.exprs.aggregators;

import com.github.zhxiaogg.jq.plan.exprs.Expression;
import com.github.zhxiaogg.jq.plan.logical.interpreter.Record;
import com.github.zhxiaogg.jq.values.Value;
import com.github.zhxiaogg.jq.values.agg.MaxAggValue;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@ToString
@EqualsAndHashCode(callSuper = true)
public class Avg extends AggExpr {
    public Avg(Expression child) {
        super(child);
    }

    @Override
    public Expression withChildren(List<Expression> children) {
        return new Max(children.get(0));
    }

    @Override
    public Value eval(Record record) {
        throw new UnsupportedOperationException("");
    }

    @Override
    public String toString() {
        return String.format("AVG(%s)", child);
    }
}
