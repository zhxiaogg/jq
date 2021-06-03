package com.github.zhxiaogg.jq.nodes.exprs.agg;

import com.github.zhxiaogg.jq.nodes.exprs.Expression;
import com.github.zhxiaogg.jq.nodes.plans.interpreter.Record;
import com.github.zhxiaogg.jq.schema.DataType;
import com.github.zhxiaogg.jq.values.LiteralValue;
import com.github.zhxiaogg.jq.values.agg.SumAggValue;
import com.github.zhxiaogg.jq.values.Value;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Collections;
import java.util.List;

@Data
@ToString
@EqualsAndHashCode
public class SumAggExpr implements Expression {
    private final Expression expression;

    public SumAggExpr(Expression expression) {
        this.expression = expression;
    }

    @Override
    public Value eval(Record record) {
        LiteralValue value = (LiteralValue) expression.eval(record);
        return SumAggValue.from(value);
    }

    @Override
    public String getDisplayName() {
        return "Sum(" + expression.getDisplayName() + ")";
    }

    @Override
    public DataType getDataType() {
        return expression.getDataType();
    }

    @Override
    public boolean leafNode() {
        return false;
    }

    @Override
    public List<Expression> getChildren() {
        return Collections.singletonList(expression);
    }

    @Override
    public Expression withChildren(List<Expression> children) {
        return new SumAggExpr(children.get(0));
    }
}
