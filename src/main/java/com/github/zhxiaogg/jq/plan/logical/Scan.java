package com.github.zhxiaogg.jq.plan.logical;

import com.github.zhxiaogg.jq.Catalog;
import com.github.zhxiaogg.jq.Relation;
import com.github.zhxiaogg.jq.plan.exec.AttributeSet;
import lombok.Data;

import java.util.Optional;

@Data
public class Scan implements LeafPlanNode {
    private final String relation;

    public static Scan from(String relation) {
        return new Scan(relation);
    }

    @Override
    public AttributeSet outputs(Catalog catalog) {
        Optional<Relation> relation = catalog.relationOf(this.relation);
        if (relation.isPresent()) {
            return relation.get().getSchema().getAttributes();
        } else {
            throw new IllegalArgumentException(String.format("relation %s not found!", relation));
        }
    }

    @Override
    public boolean leafNode() {
        return true;
    }
}
