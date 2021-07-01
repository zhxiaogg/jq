package com.github.zhxiaogg.jq.parser.utils;

import com.github.zhxiaogg.jq.ast.Expr;
import com.github.zhxiaogg.jq.ast.Where;

class WhereBuilder implements AstBuilder<Where>, AcceptExpr {
    private Expr expr;

    @Override
    public Where build() {
        return new Where(this.expr);
    }

    @Override
    public void accept(Expr expr) {
        this.expr = expr;
    }
}
