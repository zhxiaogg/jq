package com.github.zhxiaogg.jq.parser.utils;

import com.github.zhxiaogg.jq.ast.Expr;
import com.github.zhxiaogg.jq.ast.Literal;

class ExprLiteralBuilder implements ExprBuilder<Expr.ExprLiteral>, AcceptLiteral {
    private Literal literal;

    @Override
    public Expr.ExprLiteral build() {
        return new Expr.ExprLiteral(literal);
    }

    @Override
    public void accept(Literal literal) {
        this.literal = literal;
    }
}
