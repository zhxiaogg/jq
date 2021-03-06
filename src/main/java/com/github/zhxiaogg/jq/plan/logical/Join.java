package com.github.zhxiaogg.jq.plan.logical;

import com.github.zhxiaogg.jq.catalog.Catalog;
import com.github.zhxiaogg.jq.JoinType;
import com.github.zhxiaogg.jq.plan.exec.SimpleAttributeSet;
import com.github.zhxiaogg.jq.plan.exprs.Expression;
import com.github.zhxiaogg.jq.plan.exprs.booleans.BooleanExpression;
import lombok.Data;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Data
public class Join implements LogicalPlan {
    private final LogicalPlan left;
    private final LogicalPlan right;
    private final BooleanExpression constraint;
    private final JoinType joinType;

    @Override
    public boolean leafNode() {
        return false;
    }

    @Override
    public List<LogicalPlan> getChildren() {
        return Arrays.asList(left, right);
    }

    @Override
    public LogicalPlan withChildren(List<LogicalPlan> children) {
        return new Join(children.get(0), children.get(1), constraint, joinType);
    }

    @Override
    public LogicalPlan withExpressions(List<Expression> expressions) {
        return new Join(left, right, (BooleanExpression) expressions.get(0), joinType);
    }

    @Override
    public List<Expression> getExpressions() {
        return Collections.singletonList(constraint);
    }

    @Override
    public SimpleAttributeSet outputs(Catalog catalog) {
        return null;
    }
}
