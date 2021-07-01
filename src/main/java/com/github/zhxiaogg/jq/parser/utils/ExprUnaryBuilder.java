package com.github.zhxiaogg.jq.parser.utils;

import com.github.zhxiaogg.jq.ast.Expr;
import com.github.zhxiaogg.jq.ast.UnaryOp;

class ExprUnaryBuilder implements ExprBuilder<Expr.ExprUnary>, AcceptUnaryOp, AcceptExpr {
    private UnaryOp op;
    private Expr expr;

    @Override
    public Expr.ExprUnary build() {
        return new Expr.ExprUnary(op, expr);
    }

    @Override
    public void accept(Expr expr) {
        this.expr = expr;
    }

    @Override
    public void accept(UnaryOp op) {
        this.op = op;
    }
}
