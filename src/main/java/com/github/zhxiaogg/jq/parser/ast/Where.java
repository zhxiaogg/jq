package com.github.zhxiaogg.jq.parser.ast;

import lombok.Data;

@Data
public class Where implements AstNode {
    private final Expr expr;

    public interface AcceptWhere {
        void accept(Where where);
    }

    public static class WhereBuilder implements AstBuilder<Where>, Expr.AcceptExpr {
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
}
