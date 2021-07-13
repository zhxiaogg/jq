package com.github.zhxiaogg.jq.plan.logical;

import com.github.zhxiaogg.jq.Catalog;
import com.github.zhxiaogg.jq.JoinType;
import com.github.zhxiaogg.jq.plan.exprs.BooleanExpression;
import com.github.zhxiaogg.jq.plan.exprs.Expression;
import com.github.zhxiaogg.jq.plan.logical.interpreter.RecordBag;
import com.github.zhxiaogg.jq.schema.Attribute;
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
    public RecordBag partialEval(Catalog dataSource) {
        return null;
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
    public List<Attribute> getAttributes(Catalog dataSource) {
        return null;
    }
}
