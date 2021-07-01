package com.github.zhxiaogg.jq.parser.utils;

import com.github.zhxiaogg.jq.ast.Expr;
import com.github.zhxiaogg.jq.ast.FuncName;

import java.util.ArrayList;
import java.util.List;

class ExprFunctionCallBuilder implements ExprBuilder<Expr.ExprFunctionCall>, AcceptExpr, AcceptFuncName {
    private FuncName funcName;
    private final List<Expr> args = new ArrayList<>();

    @Override
    public Expr.ExprFunctionCall build() {
        return new Expr.ExprFunctionCall(funcName, args);
    }

    @Override
    public void accept(Expr expr) {
        this.args.add(expr);
    }

    @Override
    public void accept(FuncName funcName) {
        this.funcName = funcName;
    }
}
