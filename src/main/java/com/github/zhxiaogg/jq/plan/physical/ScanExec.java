package com.github.zhxiaogg.jq.plan.physical;

import com.github.zhxiaogg.jq.Relation;
import com.github.zhxiaogg.jq.plan.exec.AttributeSet;
import com.github.zhxiaogg.jq.plan.exec.RecordBag;
import com.github.zhxiaogg.jq.plan.exprs.ResolvedAttribute;
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
        return relation.records();
    }

    @Override
    public AttributeSet outputs() {
        // TODO: simplify this
        return new AttributeSet(relation.getSchema().getAttributes().toArray(new ResolvedAttribute[0]));
    }
}
