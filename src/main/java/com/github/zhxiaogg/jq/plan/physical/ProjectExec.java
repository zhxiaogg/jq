package com.github.zhxiaogg.jq.plan.physical;

import com.github.zhxiaogg.jq.plan.exec.AttributeSet;
import com.github.zhxiaogg.jq.plan.exec.Projection;
import com.github.zhxiaogg.jq.plan.exec.Record;
import com.github.zhxiaogg.jq.plan.exec.RecordBag;
import com.github.zhxiaogg.jq.plan.exprs.Expression;
import com.github.zhxiaogg.jq.plan.exprs.ResolvedAttribute;
import lombok.Data;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ProjectExec implements PhysicalPlan {
    private final List<Expression> projections;
    private final PhysicalPlan child;

    @Override
    public boolean leafNode() {
        return false;
    }

    @Override
    public List<PhysicalPlan> getChildren() {
        return Collections.singletonList(child);
    }

    @Override
    public PhysicalPlan withChildren(List<PhysicalPlan> children) {
        return new ProjectExec(projections, children.get(0));
    }

    @Override
    public RecordBag exec() {
        Projection projection = Projection.create(projections, child.outputs());
        List<Record> outputs = child.exec().getRecords().stream().map(projection::apply).collect(Collectors.toList());
        return new RecordBag(outputs);
    }

    @Override
    public AttributeSet outputs() {
        ResolvedAttribute[] attributes = projections.stream().map(Expression::toAttribute).toArray(ResolvedAttribute[]::new);
        return new AttributeSet(attributes);
    }
}
