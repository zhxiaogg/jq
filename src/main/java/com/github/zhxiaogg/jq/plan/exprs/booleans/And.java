package com.github.zhxiaogg.jq.plan.exprs.booleans;

import com.github.zhxiaogg.jq.plan.exec.Record;
import com.github.zhxiaogg.jq.plan.exprs.Expression;
import com.github.zhxiaogg.jq.plan.exprs.NonLeafExprNode;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.github.zhxiaogg.jq.utils.Requires.require;

@EqualsAndHashCode
public class And implements NonLeafExprNode, BooleanExpression {
    private final BooleanExpression left;
    private final BooleanExpression right;
    @Getter
    private final String id;

    public And(Expression left, Expression right, String id) {
        require(left instanceof BooleanExpression && right instanceof BooleanExpression, "requires boolean expressions as inputs!");
        this.left = (BooleanExpression) left;
        this.right = (BooleanExpression) right;
        this.id = id;
    }

    public And(Expression left, Expression right) {
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
        return String.format("%s AND %s", left, right);
    }

    @Override
    public boolean semanticEqual(Expression other) {
        return other instanceof And &&
                left.semanticEqual(((And) other).left) &&
                right.semanticEqual(((And) other).right);
    }

    @Override
    public Boolean evaluate(Record record) {
        return left.evaluate(record) && right.evaluate(record);
    }
}
