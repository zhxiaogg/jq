package com.github.zhxiaogg.jq.parser.utils;

import com.github.zhxiaogg.jq.ast.Expr;

class ExprAndBuilder implements ExprBuilder<Expr.ExprAnd>, AcceptExpr {
    private Expr left;
    private Expr right;

    @Override
    public Expr.ExprAnd build() {
        return new Expr.ExprAnd(left, right);
    }

    @Override
    public void accept(Expr expr) {
        if (this.left == null) this.left = expr;
        else this.right = expr;
    }
}
