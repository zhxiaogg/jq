package com.github.zhxiaogg.jq.plan.exprs.aggregators;

import com.github.zhxiaogg.jq.plan.exprs.Expression;
import com.github.zhxiaogg.jq.plan.exec.Record;
import com.github.zhxiaogg.jq.values.Value;
import com.github.zhxiaogg.jq.values.agg.MaxAggValue;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
public class Max extends AggExpr {
    public Max(Expression child, String id) {
        super(child, id);
    }

    public Max(Expression child) {
        super(child, UUID.randomUUID().toString());
    }

    @Override
    public Expression withChildren(List<Expression> children) {
        return new Max(children.get(0), id);
    }

    @Override
    public Value eval(Record record) {
        Value v = child.eval(record);
        return new MaxAggValue(v, getDataType());
    }

    @Override
    public String toString() {
        return String.format("Max(%s)", child);
    }
}
