package com.github.zhxiaogg.jq.nodes.exprs;

import com.github.zhxiaogg.jq.nodes.plans.interpreter.Record;
import com.github.zhxiaogg.jq.schema.DataType;
import com.github.zhxiaogg.jq.values.LiteralValue;
import com.github.zhxiaogg.jq.values.SumAgg;
import com.github.zhxiaogg.jq.values.Value;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Collections;
import java.util.List;

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
        return SumAgg.from(value);
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
        return new Sum(children.get(0));
    }
}
