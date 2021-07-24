package com.github.zhxiaogg.jq.analyzer.rules;

import com.github.zhxiaogg.jq.analyzer.Rule;
import com.github.zhxiaogg.jq.plan.exprs.Alias;
import com.github.zhxiaogg.jq.plan.exprs.Expression;
import com.github.zhxiaogg.jq.plan.exprs.aggregators.AggExpression;
import com.github.zhxiaogg.jq.plan.logical.Aggregate;
import com.github.zhxiaogg.jq.plan.logical.LogicalPlan;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CleanGroupByAggregatorsRule implements Rule<LogicalPlan> {
    @Override
    public Optional<LogicalPlan> apply(LogicalPlan node) {
        return node.transformUp(n -> {
            if (n instanceof Aggregate) {
                Aggregate aggregate = (Aggregate) n;
                List<Expression> groupings = new ArrayList<>(aggregate.getGroupingKeys());
                List<Expression> aggregators = aggregate.getAggregators();
                List<Expression> filtered = new ArrayList<>(aggregators.size());
                boolean groupingsChanged = false;
                for (Expression aggregator : aggregators) {
                    int groupingIdx = -1;
                    for (int i = 0; i < groupings.size(); i++) {
                        Expression grouping = groupings.get(i);
                        if (grouping.semanticEqual(aggregator) ||
                                aggregator instanceof Alias && ((Alias) aggregator).getInner().semanticEqual(grouping)) {
                            groupingIdx = i;
                            break;
                        }
                    }
                    if (groupingIdx > -1) {
                        // replacing grouping expression with aggregator, since the aggregator comes from select clause
                        groupings.set(groupingIdx, aggregator);
                        groupingsChanged = true;
                    } else if (hasAggExpression(aggregator)) {
                        filtered.add(aggregator);
                    } // else remove this expresion from aggregator
                }
                if (filtered.size() == aggregators.size() && !groupingsChanged) {
                    return Optional.empty();
                } else {
                    return Optional.of(aggregate.withAggregators(filtered).withGroupings(groupings));
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
