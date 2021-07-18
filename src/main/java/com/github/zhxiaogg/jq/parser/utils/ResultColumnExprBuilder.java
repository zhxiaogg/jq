package com.github.zhxiaogg.jq.parser.utils;

import com.github.zhxiaogg.jq.ast.Expr;
import com.github.zhxiaogg.jq.ast.ResultColumn;
import lombok.RequiredArgsConstructor;

import static com.github.zhxiaogg.jq.utils.Requires.require;

@RequiredArgsConstructor
class ResultColumnExprBuilder implements ResultColumnBuilder<ResultColumn.ResultColumnExpr>, AcceptExpr {
    private Expr expr;
    private final String alias;

    @Override
    public ResultColumn.ResultColumnExpr build() {
        if (alias != null && !alias.isEmpty()) {
            return new ResultColumn.ResultColumnExpr(new Expr.ExprAlias(expr, alias));
        }
        if (expr == null) {
            System.out.println("null");
        }
        return new ResultColumn.ResultColumnExpr(expr);
    }

    @Override
    public void accept(Expr expr) {
        require(expr != null, "");
        this.expr = expr;
    }
}
