package com.github.zhxiaogg.jq.plan.exprs.booleans;

import com.github.zhxiaogg.jq.plan.exec.Record;
import com.github.zhxiaogg.jq.plan.exprs.Expression;
import com.github.zhxiaogg.jq.plan.exprs.NonLeafExprNode;
import lombok.Data;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.github.zhxiaogg.jq.utils.Requires.require;

@Data
public class Not implements NonLeafExprNode, BooleanExpression {
    private final BooleanExpression expr;
    private final String id;

    public Not(Expression expr, String id) {
        require(expr instanceof BooleanExpression, "requires boolean expression as input!");
        this.expr = (BooleanExpression) expr;
        this.id = id;
    }

    public Not(Expression expr) {
        this(expr, UUID.randomUUID().toString());
    }

    @Override
    public List<Expression> getChildren() {
        return Collections.singletonList(expr);
    }

    @Override
    public Expression withChildren(List<Expression> children) {
        return new Not(children.get(0));
    }

    @Override
    public boolean semanticEqual(Expression other) {
        return other instanceof Not && expr.semanticEqual(((Not) other).expr);
    }

    @Override
    public Boolean evaluate(Record record) {
        return !expr.evaluate(record);
    }

    @Override
    public String toString() {
        return String.format("NOT %s", expr);
    }
}
