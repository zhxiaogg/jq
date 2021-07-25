package com.github.zhxiaogg.jq.streaming;

import com.github.zhxiaogg.jq.catalog.Catalog;
import com.github.zhxiaogg.jq.catalog.Relation;
import com.github.zhxiaogg.jq.plan.exec.RecordBag;
import com.github.zhxiaogg.jq.plan.physical.PhysicalPlan;

import java.util.Optional;

public class StreamingQuery {
    private final PhysicalPlan plan;
    private final Catalog catalog;

    public StreamingQuery(PhysicalPlan plan, Catalog catalog) {
        this.plan = plan;
        this.catalog = catalog;
    }

    public RecordBag fire(Object data) {
        Optional<Relation> relation = catalog.relationOf(data.getClass());
        if (!relation.isPresent()) throw new IllegalArgumentException("dataset not found for input data.");
        relation.get().insert(data);
        return plan.exec();
    }
}
