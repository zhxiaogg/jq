package com.github.zhxiaogg.jq.parser.ast;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    private static class JoinTargetBuilder implements TableOrSubQuery.AcceptTableOrSubQuery, JoinOp.AcceptJoinOp, JoinConstraint.AcceptJoinConstraint {
        private JoinOp joinOp;
        private TableOrSubQuery tableOrSubQuery;
        private JoinConstraint constraint;

        private void reset() {
            this.joinOp = null;
            this.tableOrSubQuery = null;
            this.constraint = null;
        }

        private Optional<JoinTarget> build() {
            if (joinOp != null && tableOrSubQuery != null) {
                return Optional.of(new JoinTarget(joinOp, tableOrSubQuery, constraint));
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

    public interface AcceptJoin {
        void accept(Join join);
    }

    public static class JoinBuilder implements AstBuilder<Join>, TableOrSubQuery.AcceptTableOrSubQuery, JoinOp.AcceptJoinOp, JoinConstraint.AcceptJoinConstraint {
        private TableOrSubQuery left;
        private final List<JoinTarget> targets = new ArrayList<>();
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
}
