package com.github.zhxiaogg.jq.stream;

import com.github.zhxiaogg.jq.Relation;
import com.github.zhxiaogg.jq.DataSource;
import com.github.zhxiaogg.jq.plans.LogicalPlan;
import com.github.zhxiaogg.jq.plans.interpreter.RecordBag;

import java.util.Optional;

public class Streaming {
    private final LogicalPlan plan;
    private final DataSource dataSource;

    public Streaming(LogicalPlan plan, DataSource dataSource) {
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
