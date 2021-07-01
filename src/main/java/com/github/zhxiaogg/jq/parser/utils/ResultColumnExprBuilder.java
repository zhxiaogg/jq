package com.github.zhxiaogg.jq.parser.utils;

import com.github.zhxiaogg.jq.ast.Expr;
import com.github.zhxiaogg.jq.ast.ResultColumn;

class ResultColumnExprBuilder implements ResultColumnBuilder<ResultColumn.ResultColumnExpr>, AcceptExpr {
    private Expr expr;

    @Override
    public ResultColumn.ResultColumnExpr build() {
        return new ResultColumn.ResultColumnExpr(expr);
    }

    @Override
    public void accept(Expr expr) {
        this.expr = expr;
    }
}
