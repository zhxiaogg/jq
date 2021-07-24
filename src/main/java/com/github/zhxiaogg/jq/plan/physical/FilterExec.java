package com.github.zhxiaogg.jq.plan.physical;

import com.github.zhxiaogg.jq.plan.exec.AttributeSet;
import com.github.zhxiaogg.jq.plan.exec.Projection;
import com.github.zhxiaogg.jq.plan.exec.Record;
import com.github.zhxiaogg.jq.plan.exec.RecordBag;
import com.github.zhxiaogg.jq.plan.exprs.booleans.BooleanExpression;
import lombok.Data;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class FilterExec implements PhysicalPlan {
    private final BooleanExpression condition;

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
        return new FilterExec(condition, children.get(0));
    }

    @Override
    public RecordBag exec() {
        Projection projection = Projection.create(Collections.singletonList(condition), child.outputs());
        List<Record> outputs = child.exec().getRecords().stream().filter(r -> {
            Record output = projection.apply(r);
            return output.getBoolean(0);
        }).collect(Collectors.toList());
        return new RecordBag(outputs);
    }

    @Override
    public AttributeSet outputs() {
        return child.outputs();
    }
}
