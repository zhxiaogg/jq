package com.github.zhxiaogg.jq.nodes.exprs.agg;

import com.github.zhxiaogg.jq.nodes.exprs.Expression;
import com.github.zhxiaogg.jq.nodes.logical.interpreter.Record;
import com.github.zhxiaogg.jq.values.Value;
import com.github.zhxiaogg.jq.values.agg.MaxAggValue;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@ToString
@EqualsAndHashCode(callSuper = true)
public class MaxAggExpr extends AggExpr {
    public MaxAggExpr(Expression child) {
        super(child);
    }

    @Override
    public Expression withChildren(List<Expression> children) {
        return new MaxAggExpr(children.get(0));
    }

    @Override
    public Value eval(Record record) {
        Value v = child.eval(record);
        return new MaxAggValue(v, getDataType());
    }

    @Override
    public String getDisplayName() {
        return String.format("Max(%s)", child.getDisplayName());
    }
}
