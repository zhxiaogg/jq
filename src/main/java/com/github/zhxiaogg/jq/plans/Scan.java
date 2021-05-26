package com.github.zhxiaogg.jq.plans;

import com.github.zhxiaogg.jq.Relation;
import com.github.zhxiaogg.jq.DataSource;
import com.github.zhxiaogg.jq.plans.interpreter.RecordBag;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Optional;

@Data
@ToString
@EqualsAndHashCode
public class Scan implements LogicalPlan {
    private final String relation;

    public static Scan from(String relation) {
        return new Scan(relation);
    }

    @Override
    public RecordBag partialEval(DataSource dataSource) {
        Optional<Relation> dataSet = dataSource.relationOf(relation);
        if (dataSet.isPresent()) {
            return dataSet.get().records();
        } else {
            throw new IllegalArgumentException("dataset not registered: " + relation);
        }
    }
}
