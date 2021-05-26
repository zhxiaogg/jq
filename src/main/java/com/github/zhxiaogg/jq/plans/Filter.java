package com.github.zhxiaogg.jq.plans;

import com.github.zhxiaogg.jq.DataSource;
import com.github.zhxiaogg.jq.exprs.BooleanExpression;
import com.github.zhxiaogg.jq.plans.interpreter.Record;
import com.github.zhxiaogg.jq.plans.interpreter.RecordBag;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@ToString
@EqualsAndHashCode
public class Filter implements LogicalPlan {
    private final BooleanExpression condition;
    private final LogicalPlan child;

    public static Filter create(BooleanExpression condition, LogicalPlan child) {
        return new Filter(condition, child);
    }

    @Override
    public RecordBag partialEval(DataSource dataSource) {
        RecordBag recordBag = child.partialEval(dataSource);
        List<Record> records = new ArrayList<>(recordBag.getRecords().size());
        for (Record r : recordBag.getRecords()) {
            if (condition.apply(r)) {
                records.add(r);
            }
        }
        return RecordBag.of(records);
    }
}
