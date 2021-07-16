package com.github.zhxiaogg.jq.plan.exprs.math;

import com.github.zhxiaogg.jq.plan.exprs.BinaryExpression;
import com.github.zhxiaogg.jq.plan.exprs.Expression;
import com.github.zhxiaogg.jq.values.Value;

import java.util.List;
import java.util.UUID;

public class ModExpr extends BinaryExpression {
    public ModExpr(Expression left, Expression right, String id) {
        super(left, right, id);
    }

    public ModExpr(Expression left, Expression right) {
        super(left, right, UUID.randomUUID().toString());
    }

    @Override
    public Expression withChildren(List<Expression> children) {
        return new ModExpr(children.get(0), children.get(1), id);
    }

    @Override
    protected Value evalImpl(Value l, Value r) {
        return null;
    }
}
