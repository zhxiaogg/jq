package com.github.zhxiaogg.jq.plan.exprs.aggregators;

import com.github.zhxiaogg.jq.plan.exprs.Expression;
import com.github.zhxiaogg.jq.plan.exprs.NonLeafExprNode;
import com.github.zhxiaogg.jq.schema.DataType;
import lombok.EqualsAndHashCode;

import java.util.Arrays;
import java.util.List;

@EqualsAndHashCode
public abstract class AggExpr implements NonLeafExprNode {
    protected final Expression child;
    protected final String id;
    protected final AggregateFunction aggregateFunction;

    public AggExpr(Expression child, String id, AggregateFunction aggregateFunction) {
        this.child = child;
        this.id = id;
        this.aggregateFunction = aggregateFunction;
    }

    @Override
    public String getId() {
        return id;
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

    @Override
    public String toString() {
        return String.format("AVG(%s)", child);
    }
}
