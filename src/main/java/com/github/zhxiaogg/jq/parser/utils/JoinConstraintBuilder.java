package com.github.zhxiaogg.jq.parser.utils;

import com.github.zhxiaogg.jq.ast.Expr;
import com.github.zhxiaogg.jq.ast.JoinConstraint;

class JoinConstraintBuilder implements AstBuilder<JoinConstraint>, AcceptExpr {
    private Expr expr;

    @Override
    public JoinConstraint build() {
        return new JoinConstraint(expr);
    }

    @Override
    public void accept(Expr expr) {
        this.expr = expr;
    }
}
