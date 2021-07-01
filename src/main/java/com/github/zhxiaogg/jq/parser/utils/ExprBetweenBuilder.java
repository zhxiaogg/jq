package com.github.zhxiaogg.jq.parser.utils;

import com.github.zhxiaogg.jq.ast.Expr;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public
class ExprBetweenBuilder implements ExprBuilder<Expr>, AcceptExpr {
    private final boolean not;
    private Expr expr;
    private Expr left;
    private Expr right;

    @Override
    public Expr build() {
        Expr.ExprBetween between = new Expr.ExprBetween(expr, left, right);
        if (not) {
            return new Expr.ExprNot(between);
        } else {
            return between;
        }
    }

    @Override
    public void accept(Expr expr) {
        if (this.expr == null) this.expr = expr;
        else if (this.left == null) this.left = expr;
        else this.right = expr;
    }

}
