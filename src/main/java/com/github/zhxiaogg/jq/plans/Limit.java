package com.github.zhxiaogg.jq.plans;

import com.github.zhxiaogg.jq.DataSource;
import com.github.zhxiaogg.jq.plans.interpreter.RecordBag;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.stream.Collectors;

@Data
@ToString
@EqualsAndHashCode
public class Limit implements LogicalPlan {
    private final int limit;
    private final LogicalPlan child;

    public Limit(int limit, LogicalPlan child) {
        this.limit = limit;
        this.child = child;
    }

    @Override
    public RecordBag partialEval(DataSource dataSource) {
        return RecordBag.of(child.partialEval(dataSource).getRecords().stream().limit(limit).collect(Collectors.toList()));
    }
}
