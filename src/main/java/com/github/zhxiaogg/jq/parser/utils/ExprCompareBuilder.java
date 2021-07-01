package com.github.zhxiaogg.jq.parser.utils;

import com.github.zhxiaogg.jq.ast.Expr;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class ExprCompareBuilder implements ExprBuilder<Expr.ExprCompare>, AcceptExpr {
    private final String op;
    private Expr left;
    private Expr right;

    @Override
    public Expr.ExprCompare build() {
        return new Expr.ExprCompare(left, right, op);
    }

    @Override
    public void accept(Expr expr) {
        if (this.left == null) this.left = expr;
        else this.right = expr;
    }
}
