package com.github.zhxiaogg.jq.plan.exprs.math;

import com.github.zhxiaogg.jq.plan.exprs.BinaryExpression;
import com.github.zhxiaogg.jq.plan.exprs.Expression;
import com.github.zhxiaogg.jq.values.Value;

import java.util.List;

public class ModExpr extends BinaryExpression {
    public ModExpr(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public Expression withChildren(List<Expression> children) {
        return new ModExpr(children.get(0), children.get(1));
    }

    @Override
    protected Value evalImpl(Value l, Value r) {
        return null;
    }
}
