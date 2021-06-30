package com.github.zhxiaogg.jq.parser.ast;

import lombok.Data;

@Data
public class JoinConstraint implements AstNode {
    private final Expr expr;

    public interface AcceptJoinConstraint {
        void accept(JoinConstraint constraint);
    }

    public static class JoinConstraintBuilder implements AstBuilder<JoinConstraint>, Expr.AcceptExpr {
        private Expr expr;

        @Override
        public JoinConstraint build() {
            return new JoinConstraint(expr);
        }

        @Override
        public void accept(Expr expr) {
            this.expr = expr;
        }
    }
}
