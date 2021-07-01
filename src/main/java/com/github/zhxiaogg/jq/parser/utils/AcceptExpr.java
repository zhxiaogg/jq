package com.github.zhxiaogg.jq.parser.utils;

import com.github.zhxiaogg.jq.ast.Expr;

interface AcceptExpr {
    void accept(Expr expr);
}
