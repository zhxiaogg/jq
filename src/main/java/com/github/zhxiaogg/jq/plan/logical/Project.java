package com.github.zhxiaogg.jq.plan.logical;

import com.github.zhxiaogg.jq.Catalog;
import com.github.zhxiaogg.jq.plan.exprs.Expression;
import com.github.zhxiaogg.jq.plan.logical.interpreter.RecordBag;
import com.github.zhxiaogg.jq.schema.Attribute;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
public class Project implements LogicalPlan {
    private final List<Expression> projections;
    private final LogicalPlan child;

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
        return new Project(projections, children.get(0));
    }

    @Override
    public RecordBag partialEval(Catalog dataSource) {
        return null;
    }

    @Override
    public LogicalPlan withExpressions(List<Expression> expressions) {
        return new Project(expressions, child);
    }

    @Override
    public List<Expression> getExpressions() {
        return new ArrayList<>(projections);
    }

    @Override
    public List<Attribute> getAttributes(Catalog dataSource) {
        return null;
    }
}
