package com.github.zhxiaogg.jq.plan.logical;

import com.github.zhxiaogg.jq.Catalog;
import com.github.zhxiaogg.jq.plan.exprs.Expression;
import com.github.zhxiaogg.jq.plan.exprs.UnResolvedAttribute;
import com.github.zhxiaogg.jq.schema.Attribute;
import com.github.zhxiaogg.jq.utils.ListUtils;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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

    @Override
    public LogicalPlan withExpressions(List<Expression> expressions) {
        List<Expression> newGroupingKeys = new ArrayList<>(groupingKeys.size());
        for (int i = 0; i < groupingKeys.size(); i++) {
            newGroupingKeys.add(expressions.get(i));
        }
        List<Expression> newAggregators = new ArrayList<>(groupingKeys.size());
        for (int i = 0; i < aggregators.size(); i++) {
            newAggregators.add(expressions.get(i + groupingKeys.size()));
        }
        return new Aggregate(newGroupingKeys, newAggregators, child);
    }

    public Aggregate withAggregators(List<Expression> aggregators) {
        return new Aggregate(this.groupingKeys, aggregators, child);
    }

    @Override
    public List<Expression> getExpressions() {
        ArrayList<Expression> expressions = new ArrayList<>(groupingKeys.size() + aggregators.size());
        expressions.addAll(groupingKeys);
        expressions.addAll(aggregators);
        return expressions;
    }

    @Override
    public List<Attribute> outputs(Catalog catalog) {
        List<Attribute> attributes1 = groupingKeys.stream().map(expr -> new Attribute(expr.getId(), expr.toString(), expr.getDataType(), null)).collect(Collectors.toList());
        List<Attribute> attributes2 = aggregators.stream().map(expr -> new Attribute(expr.getId(), expr.toString(), expr.getDataType(), null)).collect(Collectors.toList());
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
