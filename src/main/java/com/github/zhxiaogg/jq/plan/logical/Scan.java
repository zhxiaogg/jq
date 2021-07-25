package com.github.zhxiaogg.jq.plan.logical;

import com.github.zhxiaogg.jq.catalog.Catalog;
import com.github.zhxiaogg.jq.catalog.Relation;
import com.github.zhxiaogg.jq.plan.exec.SimpleAttributeSet;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Data
@RequiredArgsConstructor
public class Scan implements LeafPlanNode {
    private final String relation;
    private final String alias;

    @Override
    public SimpleAttributeSet outputs(Catalog catalog) {
        Optional<Relation> relation = catalog.relationOf(this.relation);
        if (relation.isPresent()) {
            return relation.get().getSchema().getAttributes().withName(alias);
        } else {
            throw new IllegalArgumentException(String.format("relation %s not found!", relation));
        }
    }

    @Override
    public boolean leafNode() {
        return true;
    }
}
