package com.github.zhxiaogg.jq.ast;

import lombok.Data;

@Data
public class JoinOp implements AstNode {
    private final JoinType joinType;
    private final boolean natural;

    public enum JoinType {
        LEFT,
        LEFT_OUTER,
        INNER,
        CROSS,
        JOIN,
    }
}
