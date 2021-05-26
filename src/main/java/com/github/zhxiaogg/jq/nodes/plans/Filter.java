package com.github.zhxiaogg.jq.nodes.plans;

import com.github.zhxiaogg.jq.DataSource;
import com.github.zhxiaogg.jq.nodes.exprs.BooleanExpression;
import com.github.zhxiaogg.jq.nodes.exprs.Expression;
import com.github.zhxiaogg.jq.nodes.plans.interpreter.Record;
import com.github.zhxiaogg.jq.nodes.plans.interpreter.RecordBag;
import com.github.zhxiaogg.jq.schema.Attribute;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Collections;
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

    @Override
    public LogicalPlan withExpressions(List<Expression> expressions) {
        return Filter.create((BooleanExpression) expressions.get(0), child);
    }

    @Override
    public List<Expression> getExpressions() {
        return Collections.singletonList(condition);
    }

    @Override
    public List<Attribute> getAttributes(DataSource dataSource) {
        return child.getAttributes(dataSource);
    }

    @Override
    public boolean leafNode() {
        return false;
    }

    @Override
    public List<LogicalPlan> getChildren() {
        return Collections.singletonList(child);
    }

    @Override
    public LogicalPlan withChildren(List<LogicalPlan> children) {
        return Filter.create(condition, children.get(0));
    }
}
