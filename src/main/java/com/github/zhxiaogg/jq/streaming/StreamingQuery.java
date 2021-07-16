package com.github.zhxiaogg.jq.streaming;

import com.github.zhxiaogg.jq.Relation;
import com.github.zhxiaogg.jq.Catalog;
import com.github.zhxiaogg.jq.plan.logical.LogicalPlan;
import com.github.zhxiaogg.jq.plan.exec.RecordBag;

import java.util.Optional;

public class StreamingQuery {
    private final LogicalPlan plan;
    private final Catalog dataSource;

    public StreamingQuery(LogicalPlan plan, Catalog dataSource) {
        this.plan = plan;
        this.dataSource = dataSource;
    }

    public RecordBag fire(Object data) {
        Optional<Relation> relation = dataSource.relationOf(data.getClass());
        if (!relation.isPresent()) throw new IllegalArgumentException("dataset not found for input data.");
        relation.get().add(data);
        return plan.partialEval(dataSource);
    }
}
