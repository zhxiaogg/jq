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
public class Between implements NonLeafExprNode, BooleanExpression {
    private final Expression target;
    private final Expression left;
    private final Expression right;
    private final String id;

    public Between(Expression target, Expression left, Expression right) {
        this(target, left, right, UUID.randomUUID().toString());
    }

    @Override
    public List<Expression> getChildren() {
        return Arrays.asList(target, left, right);
    }

    @Override
    public Expression withChildren(List<Expression> children) {
        return new Between(children.get(0), children.get(1), children.get(2), id);
    }

    @Override
    public boolean semanticEqual(Expression other) {
        return other instanceof Between &&
                left.semanticEqual(((Between) other).left) &&
                right.semanticEqual(((Between) other).right) &&
                target.semanticEqual(((Between) other).target);
    }

    @Override
    public Boolean evaluate(Record record) {
        return null;
    }

    @Override
    public String toString() {
        return null;
    }
}
