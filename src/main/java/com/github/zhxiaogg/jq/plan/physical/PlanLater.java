package com.github.zhxiaogg.jq.plan.physical;

import com.github.zhxiaogg.jq.plan.exec.AttributeSet;
import com.github.zhxiaogg.jq.plan.exec.RecordBag;
import com.github.zhxiaogg.jq.plan.logical.LogicalPlan;
import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class PlanLater implements PhysicalPlan {
    private final LogicalPlan plan;

    @Override
    public boolean leafNode() {
        return false;
    }

    @Override
    public List<PhysicalPlan> getChildren() {
        return Collections.emptyList();
    }

    @Override
    public PhysicalPlan withChildren(List<PhysicalPlan> children) {
        return this;
    }

    @Override
    public RecordBag exec() {
        throw new IllegalStateException("unexecutable!");
    }

    @Override
    public AttributeSet outputs() {
        return null;
    }
}
