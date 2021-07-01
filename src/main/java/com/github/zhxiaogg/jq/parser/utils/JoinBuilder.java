package com.github.zhxiaogg.jq.parser.utils;

import com.github.zhxiaogg.jq.ast.Join;
import com.github.zhxiaogg.jq.ast.JoinConstraint;
import com.github.zhxiaogg.jq.ast.JoinOp;
import com.github.zhxiaogg.jq.ast.TableOrSubQuery;

import java.util.ArrayList;
import java.util.List;

class JoinBuilder implements AstBuilder<Join>, AcceptTableOrSubQuery, AcceptJoinOp, AcceptJoinConstraint {
    private TableOrSubQuery left;
    private final List<Join.JoinTarget> targets = new ArrayList<>();
    private final JoinTargetBuilder builder = new JoinTargetBuilder();

    @Override
    public Join build() {
        builder.build().ifPresent(targets::add);
        return new Join(left, targets);
    }

    @Override
    public void accept(JoinConstraint constraint) {
        builder.accept(constraint);
    }

    @Override
    public void accept(JoinOp joinOp) {
        builder.build().ifPresent(targets::add);
        builder.reset();
        builder.accept(joinOp);
    }

    @Override
    public void accept(TableOrSubQuery node) {
        if (left == null) {
            this.left = node;
        } else {
            builder.accept(node);
        }
    }
}
