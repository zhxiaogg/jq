package com.github.zhxiaogg.jq.parser.ast;

import lombok.Data;

@Data
public class Having implements AstNode {
    private final Expr expr;

    public interface AcceptHaving {
        void accept(Having having);
    }

    public static class HavingBuilder implements AstBuilder<Having>, Expr.AcceptExpr {
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
}
