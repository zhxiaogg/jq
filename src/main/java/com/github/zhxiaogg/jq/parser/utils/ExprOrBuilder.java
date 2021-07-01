package com.github.zhxiaogg.jq.parser.utils;

import com.github.zhxiaogg.jq.ast.Expr;

class ExprOrBuilder implements ExprBuilder<Expr.ExprOr>, AcceptExpr {
    private Expr left;
    private Expr right;

    @Override
    public Expr.ExprOr build() {
        return new Expr.ExprOr(left, right);
    }

    @Override
    public void accept(Expr expr) {
        if (this.left == null) this.left = expr;
        else this.right = expr;
    }
}
