package com.github.zhxiaogg.jq.plan.exprs.booleans;

import com.github.zhxiaogg.jq.plan.exec.Record;
import com.github.zhxiaogg.jq.plan.exprs.Expression;
import com.github.zhxiaogg.jq.plan.exprs.NonLeafExprNode;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static com.github.zhxiaogg.jq.utils.Requires.require;

@EqualsAndHashCode
public class Or implements NonLeafExprNode, BooleanExpression {
    private final BooleanExpression left;
    private final BooleanExpression right;
    @Getter
    private final String id;

    public Or(Expression left, Expression right, String id) {
        require(left instanceof BooleanExpression && right instanceof BooleanExpression, "requires boolean expressions as inputs!");
        this.left = (BooleanExpression) left;
        this.right = (BooleanExpression) right;
        this.id = id;
    }

    public Or(Expression left, Expression right) {
        this(left, right, UUID.randomUUID().toString());
    }

    @Override
    public Expression withChildren(List<Expression> children) {
        return new And(children.get(0), children.get(1), id);
    }

    @Override
    public List<Expression> getChildren() {
        return Arrays.asList(left, right);
    }

    @Override
    public String toString() {
        return String.format("%s OR %s", left, right);
    }

    @Override
    public boolean apply(Record record) {
        return left.apply(record) || right.apply(record);
    }
}
