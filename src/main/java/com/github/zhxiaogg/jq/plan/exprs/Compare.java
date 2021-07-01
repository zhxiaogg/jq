package com.github.zhxiaogg.jq.plan.exprs;

import com.github.zhxiaogg.jq.plan.logical.interpreter.Record;
import com.github.zhxiaogg.jq.schema.DataType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Arrays;
import java.util.List;

@Data
@ToString
@EqualsAndHashCode
public class Compare implements BooleanExpression {
    private final CompareOp op;
    private final Expression left;
    private final Expression right;

    public Compare(CompareOp op, Expression left, Expression right) {
        this.op = op;
        this.left = left;
        this.right = right;
    }


    @Override
    public boolean apply(Record record) {
        return op.apply(left.eval(record), right.eval(record));
    }

    @Override
    public List<Expression> getChildren() {
        return Arrays.asList(left, right);
    }

    @Override
    public Expression withChildren(List<Expression> children) {
        return new Compare(op, children.get(0), children.get(1));
    }

    @Override
    public String getDisplayName() {
        return left.getDisplayName() + "_" + op.name() + "_" + right.getDisplayName();
    }

    @Override
    public DataType getDataType() {
        return DataType.Boolean;
    }
}
