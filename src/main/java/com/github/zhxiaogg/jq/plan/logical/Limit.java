package com.github.zhxiaogg.jq.plan.logical;

import com.github.zhxiaogg.jq.Catalog;
import com.github.zhxiaogg.jq.plan.exprs.Expression;
import com.github.zhxiaogg.jq.plan.logical.interpreter.RecordBag;
import com.github.zhxiaogg.jq.schema.Attribute;
import lombok.Data;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class Limit implements LogicalPlan {
    private final int limit;
    private final LogicalPlan child;

    public Limit(int limit, LogicalPlan child) {
        this.limit = limit;
        this.child = child;
    }

    @Override
    public RecordBag partialEval(Catalog dataSource) {
        return RecordBag.of(child.partialEval(dataSource).getRecords().stream().limit(limit).collect(Collectors.toList()));
    }

    @Override
    public LogicalPlan withExpressions(List<Expression> expressions) {
        throw new IllegalStateException("");
    }

    @Override
    public List<Expression> getExpressions() {
        return Collections.emptyList();
    }

    @Override
    public List<Attribute> getAttributes(Catalog dataSource) {
        return null;
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
        return new Limit(limit, children.get(0));
    }
}
