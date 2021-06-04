package com.github.zhxiaogg.jq.nodes.logical;

import com.github.zhxiaogg.jq.Catalog;
import com.github.zhxiaogg.jq.nodes.exprs.Expression;
import com.github.zhxiaogg.jq.nodes.logical.interpreter.RecordBag;
import com.github.zhxiaogg.jq.schema.Attribute;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;
import java.util.stream.Collectors;

@Data
@ToString
@EqualsAndHashCode
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
        return null;
    }

    @Override
    public List<Expression> getExpressions() {
        return null;
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
        return null;
    }

    @Override
    public LogicalPlan withChildren(List<LogicalPlan> children) {
        return null;
    }
}
