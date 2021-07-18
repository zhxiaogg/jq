package com.github.zhxiaogg.jq.analyzer.rules;

import com.github.zhxiaogg.jq.analyzer.Rule;
import com.github.zhxiaogg.jq.plan.exprs.Expression;
import com.github.zhxiaogg.jq.plan.exprs.aggregators.AggExpression;
import com.github.zhxiaogg.jq.plan.logical.Aggregate;
import com.github.zhxiaogg.jq.plan.logical.LogicalPlan;

import java.util.*;

public class CleanGroupByAggregatorsRule implements Rule<LogicalPlan> {
    @Override
    public Optional<LogicalPlan> apply(LogicalPlan node) {
        return node.transformUp(n -> {
            if (n instanceof Aggregate) {
                Aggregate aggregate = (Aggregate) n;
                Set<Expression> groupings = new HashSet<>(aggregate.getGroupingKeys());
                List<Expression> aggregators = aggregate.getAggregators();
                List<Expression> filtered = new ArrayList<>(aggregators.size());
                for (Expression aggregator : aggregators) {
                    if (hasAggExpression(aggregator) || !groupings.contains(aggregator)) {
                        filtered.add(aggregator);
                    } // else remove this expresion from aggregator
                }
                if (filtered.size() == aggregators.size()) {
                    return Optional.empty();
                } else {
                    return Optional.of(aggregate.withAggregators(filtered));
                }
            } else {
                return Optional.empty();
            }
        });
    }

    private boolean hasAggExpression(Expression e) {
        if (e instanceof AggExpression) {
            return true;
        } else {
            return e.getChildren().stream().anyMatch(this::hasAggExpression);
        }
    }
}
