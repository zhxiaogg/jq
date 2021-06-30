package com.github.zhxiaogg.jq.parser.ast;

import lombok.Data;

@Data
public class JoinOp implements AstNode, AstBuilder<JoinOp> {
    private final JoinType joinType;
    private final boolean natrual;

    @Override
    public JoinOp build() {
        return this;
    }

    public interface AcceptJoinOp {
        void accept(JoinOp joinOp);
    }

    public enum JoinType {
        LEFT,
        LEFT_OUTER,
        INNER,
        CROSS,
        JOIN,
    }
}
