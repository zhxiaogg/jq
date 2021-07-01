package com.github.zhxiaogg.jq.parser.utils;

import com.github.zhxiaogg.jq.ast.Expr;
import com.github.zhxiaogg.jq.ast.Select;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
class ExprInBuilder implements ExprBuilder<Expr>, AcceptExpr, AcceptSelect {
    private final boolean not;
    private Expr expr;
    private final List<Expr> exprs = new ArrayList<>();
    private Select select;

    @Override
    public Expr build() {
        Expr.ExprIn in = new Expr.ExprIn(expr, exprs, select);
        if (not) {
            return new Expr.ExprNot(in);
        } else {
            return in;
        }
    }

    @Override
    public void accept(Expr expr) {
        if (this.expr == null) this.expr = expr;
        else this.exprs.add(expr);
    }

    @Override
    public void accept(Select select) {
        this.select = select;
    }
}
