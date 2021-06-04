package com.github.zhxiaogg.jq.nodes.exprs.agg;

import com.github.zhxiaogg.jq.nodes.exprs.Expression;
import com.github.zhxiaogg.jq.nodes.logical.interpreter.Record;
import com.github.zhxiaogg.jq.values.Value;
import com.github.zhxiaogg.jq.values.agg.MinAggValue;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@ToString
@EqualsAndHashCode(callSuper = true)
public class MinAggExpr extends AggExpr {
    public MinAggExpr(Expression child) {
        super(child);
    }

    @Override
    public Expression withChildren(List<Expression> children) {
        return new MinAggExpr(children.get(0));
    }

    @Override
    public Value eval(Record record) {
        Value v = child.eval(record);
        return new MinAggValue(v, getDataType());
    }

    @Override
    public String getDisplayName() {
        return String.format("Min(%s)", child.getDisplayName());
    }
}
