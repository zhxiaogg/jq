package com.github.zhxiaogg.jq.plan.exprs;

import com.github.zhxiaogg.jq.plan.exec.Record;
import com.github.zhxiaogg.jq.schema.DataType;
import com.github.zhxiaogg.jq.values.Value;

import java.util.Arrays;
import java.util.List;

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
    public String getId() {
        return id;
    }

    @Override
    public List<Expression> getChildren() {
        return Arrays.asList(left, right);
    }

    @Override
    public Value eval(Record record) {
        Value l = left.eval(record);
        Value r = right.eval(record);
        return evalImpl(l, r);
    }

    @Override
    public Object evaluate(Record record) {
        return eval(record).getValue();
    }

    abstract protected Value evalImpl(Value l, Value r);

    @Override
    public String toString() {
        return String.format("%s + %s", left.toString(), right.toString());
    }

    @Override
    public DataType getDataType() {
        return left.getDataType();
    }
}
