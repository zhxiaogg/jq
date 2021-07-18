package com.github.zhxiaogg.jq.plan.logical;

import com.github.zhxiaogg.jq.Catalog;
import com.github.zhxiaogg.jq.plan.exec.AttributeSet;
import com.github.zhxiaogg.jq.plan.exec.Projection;
import com.github.zhxiaogg.jq.plan.exec.Record;
import com.github.zhxiaogg.jq.plan.exec.RecordBag;
import com.github.zhxiaogg.jq.plan.exprs.Expression;
import com.github.zhxiaogg.jq.schema.Attribute;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
    public RecordBag partialEval(Catalog catalog) {
        RecordBag inputs = child.partialEval(catalog);
        Projection projection = Projection.create(projections, new AttributeSet(child.outputs(catalog).toArray(new Attribute[0])));
        List<Record> outputs = inputs.getRecords().stream().map(projection::apply).collect(Collectors.toList());
        return new RecordBag(outputs);

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
    public List<Attribute> outputs(Catalog catalog) {
        return projections.stream().map(Expression::toAttribute).collect(Collectors.toList());
    }
}
