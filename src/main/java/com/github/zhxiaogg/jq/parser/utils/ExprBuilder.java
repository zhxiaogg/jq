package com.github.zhxiaogg.jq.parser.utils;

import com.github.zhxiaogg.jq.ast.Expr;

interface ExprBuilder<T extends Expr> extends AstBuilder<T> {
    @Override
    T build();
}
