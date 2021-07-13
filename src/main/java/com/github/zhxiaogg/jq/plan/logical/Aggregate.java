package com.github.zhxiaogg.jq.plan.logical;

import com.github.zhxiaogg.jq.Catalog;
import com.github.zhxiaogg.jq.plan.exprs.Expression;
import com.github.zhxiaogg.jq.plan.exprs.UnResolvedAttribute;
import com.github.zhxiaogg.jq.plan.logical.interpreter.Record;
import com.github.zhxiaogg.jq.plan.logical.interpreter.RecordBag;
import com.github.zhxiaogg.jq.schema.Attribute;
import com.github.zhxiaogg.jq.utils.ListUtils;
import com.github.zhxiaogg.jq.values.AggValue;
import com.github.zhxiaogg.jq.values.LiteralValue;
import com.github.zhxiaogg.jq.values.Value;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

@Data
public class Aggregate implements LogicalPlan {
    private final List<Expression> groupingKeys;
    private final List<Expression> aggregators;
    private final LogicalPlan child;

    @Deprecated
    public static Aggregate create(List<String> groupingKeys, List<Expression> aggregators, LogicalPlan child) {
        return new Aggregate(groupingKeys.stream().map(k -> new UnResolvedAttribute(null, k)).collect(Collectors.toList()), aggregators, child);
    }

    private final Map<List<LiteralValue>, List<AggValue>> states = new HashMap<>();

    @Override
    public RecordBag partialEval(Catalog dataSource) {
        RecordBag recordBag = child.partialEval(dataSource);
        List<Record> rs = new ArrayList<>();
        for (Record record : recordBag.getRecords()) {
            // assembly a record based on  groupingKeys and aggregators
            List<LiteralValue> keys = groupingKeys.stream().map(e -> (LiteralValue) e.eval(record)).collect(Collectors.toList());
            List<AggValue> aggregated = aggregators.stream().map(e -> (AggValue) e.eval(record)).collect(Collectors.toList());
            List<AggValue> existing = states.get(keys);
            final Record r;
            if (existing == null) {
                states.put(keys, aggregated);
                r = Record.create((List<Value>) (ListUtils.concat((List) keys, (List) aggregated)));
            } else {
                List<AggValue> updated = new ArrayList<>(existing.size());
                for (int i = 0; i < existing.size(); i++) {
                    AggValue merged = existing.get(i).merge(aggregated.get(i));
                    updated.add(merged);
                }
                states.put(keys, updated);
                r = Record.create((List<Value>) (ListUtils.concat((List) keys, (List) updated)));
            }
            // only emit those updated records
            rs.add(r);
        }
        return RecordBag.of(rs);
    }

    @Override
    public LogicalPlan withExpressions(List<Expression> expressions) {
        List<Expression> newGroupingKeys = new ArrayList<>(groupingKeys.size());
        for (int i = 0; i < groupingKeys.size(); i++) {
            newGroupingKeys.add(expressions.get(i));
        }
        List<Expression> newAggregators = new ArrayList<>(groupingKeys.size());
        for (int i = 0; i < groupingKeys.size(); i++) {
            newAggregators.add(expressions.get(i + groupingKeys.size()));
        }
        return new Aggregate(newGroupingKeys, newAggregators, child);
    }

    @Override
    public List<Expression> getExpressions() {
        ArrayList<Expression> expressions = new ArrayList<>(groupingKeys.size() + aggregators.size());
        expressions.addAll(groupingKeys);
        expressions.addAll(aggregators);
        return expressions;
    }

    @Override
    public List<Attribute> getAttributes(Catalog dataSource) {
        List<Attribute> attributes1 = groupingKeys.stream().map(expr -> new Attribute(expr.toString(), expr.getDataType(), null)).collect(Collectors.toList());
        List<Attribute> attributes2 = aggregators.stream().map(expr -> new Attribute(expr.toString(), expr.getDataType(), null)).collect(Collectors.toList());
        return ListUtils.concat(attributes1, attributes2);
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
        return new Aggregate(groupingKeys, aggregators, children.get(0));
    }
}
