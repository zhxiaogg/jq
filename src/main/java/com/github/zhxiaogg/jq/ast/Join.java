package com.github.zhxiaogg.jq.ast;

import lombok.Data;

import java.util.List;

@Data
public class Join implements AstNode {
    private final TableOrSubQuery left;
    private final List<JoinTarget> targets;

    @Data
    public static class JoinTarget implements AstNode {
        private final JoinOp joinOp;
        private final TableOrSubQuery tableOrSubQuery;
        private final JoinConstraint constraint;
    }

}
