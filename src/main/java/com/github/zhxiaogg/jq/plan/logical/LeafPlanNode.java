package com.github.zhxiaogg.jq.plan.logical;

import com.github.zhxiaogg.jq.plan.exprs.Expression;

import java.util.Collections;
import java.util.List;

public interface LeafPlanNode extends LogicalPlan {

    default LogicalPlan withExpressions(List<Expression> expressions) {
        throw new IllegalStateException();
    }

    default List<Expression> getExpressions() {
        throw new IllegalStateException();
    }

    default boolean leafNode() {
        return true;
    }

    default List<LogicalPlan> getChildren() {
        return Collections.emptyList();
    }

    default LogicalPlan withChildren(List<LogicalPlan> children) {
        throw new IllegalStateException();
    }
}
