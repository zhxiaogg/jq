package com.github.zhxiaogg.jq.plan.exprs;

import com.github.zhxiaogg.jq.plan.exec.Record;
import com.github.zhxiaogg.jq.schema.DataType;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public abstract class BinaryExpression implements NonLeafExprNode {
    protected final Expression left;
    protected final Expression right;
    protected final String id;

    public BinaryExpression(Expression left, Expression right, String id) {
        this.left = left;
        this.right = right;
        this.id = id;
    }

    @Override
    public boolean semanticEqual(Expression other) {
        return Objects.nonNull(other) &&
                other.getClass() == this.getClass() &&
                this.left.semanticEqual(((BinaryExpression) other).left) &&
                this.right.semanticEqual(((BinaryExpression) other).right);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public List<Expression> getChildren() {
        return Arrays.asList(left, right);
    }

    @Override
    public Object evaluate(Record record) {
        Object l = left.evaluate(record);
        Object r = right.evaluate(record);
        return evalImpl(l, r);
    }

    abstract protected Object evalImpl(Object l, Object r);

    @Override
    public String toString() {
        return String.format("%s + %s", left.toString(), right.toString());
    }

    @Override
    public DataType getDataType() {
        return left.getDataType();
    }
}
