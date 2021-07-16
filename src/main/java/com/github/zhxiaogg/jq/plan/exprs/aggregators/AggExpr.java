package com.github.zhxiaogg.jq.plan.exprs.aggregators;

import com.github.zhxiaogg.jq.plan.exprs.Expression;
import com.github.zhxiaogg.jq.plan.exprs.NonLeafExprNode;
import com.github.zhxiaogg.jq.schema.DataType;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@EqualsAndHashCode
public abstract class AggExpr implements NonLeafExprNode {
    protected final Expression child;
    protected final String id;

    public AggExpr(Expression child, String id) {
        this.child = child;
        this.id = id;
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
