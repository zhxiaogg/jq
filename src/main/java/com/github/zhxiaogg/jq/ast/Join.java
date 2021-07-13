package com.github.zhxiaogg.jq.ast;

import com.github.zhxiaogg.jq.JoinType;
import com.github.zhxiaogg.jq.plan.exprs.BooleanExpression;
import com.github.zhxiaogg.jq.plan.logical.LogicalPlan;
import lombok.Data;

import java.util.List;

@Data
public class Join implements AstNode {
    private final TableOrSubQuery left;
    private final List<JoinTarget> targets;

    // TODO: this looks wired.
    @Data
    public static class JoinTarget implements AstNode {
        private final JoinOp joinOp;
        private final TableOrSubQuery tableOrSubQuery;
        private final JoinConstraint constraint;
    }

    LogicalPlan toPlanNode() {
        LogicalPlan plan = this.left.toPlanNode();
        for (JoinTarget target : targets) {
            JoinType joinType = target.getJoinOp() == null ? JoinType.INNER : target.getJoinOp().getJoinType();
            LogicalPlan right = target.getTableOrSubQuery().toPlanNode();
            BooleanExpression constraint = target.getConstraint() == null ? null : (BooleanExpression) target.getConstraint().getExpr().toExpression();
            plan = new com.github.zhxiaogg.jq.plan.logical.Join(plan, right, constraint, joinType);
        }
        return plan;
    }

}
