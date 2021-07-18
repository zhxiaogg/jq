package com.github.zhxiaogg.jq.streaming;

import com.github.zhxiaogg.jq.Catalog;
import com.github.zhxiaogg.jq.Relation;
import com.github.zhxiaogg.jq.plan.exec.RecordBag;
import com.github.zhxiaogg.jq.plan.logical.LogicalPlan;

import java.util.Optional;

public class StreamingQuery {
    private final LogicalPlan plan;
    private final Catalog catalog;

    public StreamingQuery(LogicalPlan plan, Catalog catalog) {
        this.plan = plan;
        this.catalog = catalog;
    }

    public RecordBag fire(Object data) {
        Optional<Relation> relation = catalog.relationOf(data.getClass());
        if (!relation.isPresent()) throw new IllegalArgumentException("dataset not found for input data.");
        relation.get().add(data);
        return plan.partialEval(catalog);
    }
}
