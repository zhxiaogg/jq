package com.github.zhxiaogg.jq.nodes.exprs.agg;

import com.github.zhxiaogg.jq.nodes.exprs.Expression;
import com.github.zhxiaogg.jq.nodes.logical.interpreter.Record;
import com.github.zhxiaogg.jq.values.LiteralValue;
import com.github.zhxiaogg.jq.values.Value;
import com.github.zhxiaogg.jq.values.agg.SumAggValue;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@ToString
@EqualsAndHashCode(callSuper = true)
public class SumAggExpr extends AggExpr {

    public SumAggExpr(Expression child) {
        super(child);
    }

    @Override
    public Value eval(Record record) {
        LiteralValue value = (LiteralValue) child.eval(record);
        return SumAggValue.from(value);
    }

    @Override
    public String getDisplayName() {
        return "Sum(" + child.getDisplayName() + ")";
    }

    @Override
    public Expression withChildren(List<Expression> children) {
        return new SumAggExpr(children.get(0));
    }
}
