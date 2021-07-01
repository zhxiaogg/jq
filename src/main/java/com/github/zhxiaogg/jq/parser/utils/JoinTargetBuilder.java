package com.github.zhxiaogg.jq.parser.utils;

import com.github.zhxiaogg.jq.ast.Join;
import com.github.zhxiaogg.jq.ast.JoinConstraint;
import com.github.zhxiaogg.jq.ast.JoinOp;
import com.github.zhxiaogg.jq.ast.TableOrSubQuery;

import java.util.Optional;

class JoinTargetBuilder implements AcceptTableOrSubQuery, AcceptJoinOp, AcceptJoinConstraint {
    private JoinOp joinOp;
    private TableOrSubQuery tableOrSubQuery;
    private JoinConstraint constraint;

    public void reset() {
        this.joinOp = null;
        this.tableOrSubQuery = null;
        this.constraint = null;
    }

    public Optional<Join.JoinTarget> build() {
        if (joinOp != null && tableOrSubQuery != null) {
            return Optional.of(new Join.JoinTarget(joinOp, tableOrSubQuery, constraint));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public void accept(JoinConstraint constraint) {
        this.constraint = constraint;
    }

    @Override
    public void accept(JoinOp joinOp) {
        this.joinOp = joinOp;
    }

    @Override
    public void accept(TableOrSubQuery node) {
        this.tableOrSubQuery = node;
    }
}
