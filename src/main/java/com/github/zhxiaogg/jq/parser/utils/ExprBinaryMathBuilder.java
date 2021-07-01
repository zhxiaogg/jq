package com.github.zhxiaogg.jq.parser.utils;

import com.github.zhxiaogg.jq.ast.Expr;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public
class ExprBinaryMathBuilder implements ExprBuilder<Expr.ExprBinaryMath>, AcceptExpr {
    private final String op;
    private Expr left;
    private Expr right;

    @Override
    public Expr.ExprBinaryMath build() {
        return new Expr.ExprBinaryMath(left, right, op);
    }

    @Override
    public void accept(Expr expr) {
        if (this.left == null) this.left = expr;
        else this.right = expr;
    }
}
