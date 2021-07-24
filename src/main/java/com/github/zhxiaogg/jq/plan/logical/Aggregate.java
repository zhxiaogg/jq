package com.github.zhxiaogg.jq.plan.logical;

import com.github.zhxiaogg.jq.Catalog;
import com.github.zhxiaogg.jq.plan.exec.AttributeSet;
import com.github.zhxiaogg.jq.plan.exec.SimpleAttributeSet;
import com.github.zhxiaogg.jq.plan.exprs.Expression;
import com.github.zhxiaogg.jq.plan.exprs.ResolvedAttribute;
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
    public AttributeSet outputs(Catalog catalog) {
        List<ResolvedAttribute> attributes1 = groupingKeys.stream().map(Expression::toAttribute).collect(Collectors.toList());
        List<ResolvedAttribute> attributes2 = aggregators.stream().map(Expression::toAttribute).collect(Collectors.toList());
        return AttributeSet.create(ListUtils.concat(attributes1, attributes2));
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
