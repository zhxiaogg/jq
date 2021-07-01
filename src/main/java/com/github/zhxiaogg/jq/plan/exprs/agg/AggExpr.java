package com.github.zhxiaogg.jq.plan.exprs.agg;

import com.github.zhxiaogg.jq.plan.exprs.Expression;
import com.github.zhxiaogg.jq.schema.DataType;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Arrays;
import java.util.List;

@EqualsAndHashCode
@ToString
public abstract class AggExpr implements Expression {
    protected final Expression child;

    public AggExpr(Expression child) {
        this.child = child;
    }

    @Override
    public boolean leafNode() {
        return false;
    }

    @Override
    public List<Expression> getChildren() {
        return Arrays.asList(child);
    }

    @Override
    public DataType getDataType() {
        return child.getDataType();
    }
}
