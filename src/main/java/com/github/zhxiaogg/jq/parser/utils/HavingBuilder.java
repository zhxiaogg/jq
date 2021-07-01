package com.github.zhxiaogg.jq.parser.utils;

import com.github.zhxiaogg.jq.ast.Expr;
import com.github.zhxiaogg.jq.ast.Having;

class HavingBuilder implements AstBuilder<Having>, AcceptExpr {
    private Expr expr;

    @Override
    public Having build() {
        return new Having(expr);
    }

    @Override
    public void accept(Expr expr) {
        this.expr = expr;
    }
}
