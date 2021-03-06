package com.github.zhxiaogg.jq.plan.exprs.aggregators;

import com.github.zhxiaogg.jq.datatypes.DataType;
import com.github.zhxiaogg.jq.plan.exec.Record;
import com.github.zhxiaogg.jq.plan.exprs.Expression;
import com.github.zhxiaogg.jq.plan.exprs.NonLeafExprNode;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

@Data
public abstract class AggExpression implements NonLeafExprNode {
    protected final Expression child;
    protected final String id;
    protected final AggregateFunction aggregateFunction;

    @Override
    public boolean semanticEqual(Expression other) {
        return other != null && this.getClass() == other.getClass() && this.child.semanticEqual(((AggExpression) other).child);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Object evaluate(Record record) {
        throw new IllegalStateException("unsupported!");
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
