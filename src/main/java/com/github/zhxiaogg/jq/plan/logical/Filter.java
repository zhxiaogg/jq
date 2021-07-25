package com.github.zhxiaogg.jq.plan.logical;

import com.github.zhxiaogg.jq.catalog.Catalog;
import com.github.zhxiaogg.jq.plan.exec.AttributeSet;
import com.github.zhxiaogg.jq.plan.exprs.Expression;
import com.github.zhxiaogg.jq.plan.exprs.booleans.BooleanExpression;
import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class Filter implements LogicalPlan {
    private final BooleanExpression condition;
    private final LogicalPlan child;

    public static Filter create(BooleanExpression condition, LogicalPlan child) {
        return new Filter(condition, child);
    }

    @Override
    public LogicalPlan withExpressions(List<Expression> expressions) {
        return Filter.create((BooleanExpression) expressions.get(0), child);
    }

    @Override
    public List<Expression> getExpressions() {
        return Collections.singletonList(condition);
    }

    @Override
    public AttributeSet outputs(Catalog catalog) {
        return child.outputs(catalog);
    }

    @Override
    public boolean leafNode() {
        return false;
    }

    @Override
    public List<LogicalPlan> getChildren() {
        return Collections.singletonList(child);
    }

    @Override
    public LogicalPlan withChildren(List<LogicalPlan> children) {
        return Filter.create(condition, children.get(0));
    }
}
