package com.github.zhxiaogg.jq.plan.logical;

import com.github.zhxiaogg.jq.Catalog;
import com.github.zhxiaogg.jq.plan.exec.RecordBag;
import com.github.zhxiaogg.jq.plan.exprs.Expression;
import com.github.zhxiaogg.jq.schema.Attribute;
import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class SubQuery implements LogicalPlan {
    private final LogicalPlan subQuery;

    @Override
    public boolean leafNode() {
        return false;
    }

    @Override
    public List<LogicalPlan> getChildren() {
        return Collections.singletonList(subQuery);
    }

    @Override
    public LogicalPlan withChildren(List<LogicalPlan> children) {
        return new SubQuery(children.get(0));
    }

    @Override
    public RecordBag partialEval(Catalog dataSource) {
        return null;
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
    public List<Attribute> outputs(Catalog dataSource) {
        return subQuery.outputs(dataSource);
    }
}
