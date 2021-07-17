package com.github.zhxiaogg.jq.plan.exprs.booleans;

import com.github.zhxiaogg.jq.plan.exec.Record;
import com.github.zhxiaogg.jq.plan.exprs.Expression;
import com.github.zhxiaogg.jq.plan.exprs.NonLeafExprNode;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Data
@RequiredArgsConstructor
public class Compare implements NonLeafExprNode, BooleanExpression {
    private final CompareOp op;
    private final Expression left;
    private final Expression right;
    private final String id;

    public Compare(CompareOp op, Expression left, Expression right) {
        this(op, left, right, UUID.randomUUID().toString());
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
    public String toString() {
        return left + "_" + op.name() + "_" + right;
    }
}
