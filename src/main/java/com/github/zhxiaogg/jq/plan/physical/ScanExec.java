package com.github.zhxiaogg.jq.plan.physical;

import com.github.zhxiaogg.jq.catalog.Relation;
import com.github.zhxiaogg.jq.plan.exec.AttributeSet;
import com.github.zhxiaogg.jq.plan.exec.RecordBag;
import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class ScanExec implements PhysicalPlan {
    private final Relation relation;

    @Override
    public boolean leafNode() {
        return true;
    }

    @Override
    public List<PhysicalPlan> getChildren() {
        return Collections.emptyList();
    }

    @Override
    public ScanExec withChildren(List<PhysicalPlan> children) {
        return this;
    }

    @Override
    public RecordBag exec() {
        return relation.scan();
    }

    @Override
    public AttributeSet outputs() {
        return relation.getSchema().getAttributes();
    }
}
