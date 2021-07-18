package com.github.zhxiaogg.jq.analyzer.rules;

import com.github.zhxiaogg.jq.Catalog;
import com.github.zhxiaogg.jq.analyzer.Rule;
import com.github.zhxiaogg.jq.plan.exec.AttributeSet;
import com.github.zhxiaogg.jq.plan.exprs.Expression;
import com.github.zhxiaogg.jq.plan.exprs.UnResolvedAttribute;
import com.github.zhxiaogg.jq.plan.exprs.aggregators.AggExpression;
import com.github.zhxiaogg.jq.plan.exprs.booleans.BooleanExpression;
import com.github.zhxiaogg.jq.plan.logical.Aggregate;
import com.github.zhxiaogg.jq.plan.logical.Filter;
import com.github.zhxiaogg.jq.plan.logical.LogicalPlan;
import com.github.zhxiaogg.jq.plan.logical.Project;
import com.github.zhxiaogg.jq.schema.Attribute;
import com.github.zhxiaogg.jq.utils.Pair;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Should push {@link Filter#getCondition()} components into {@link Aggregate#getAggregators()} if the condition expression
 * cannot be resolved. A {@link com.github.zhxiaogg.jq.plan.logical.Project} will also be created simultaneously.
 */
@RequiredArgsConstructor
public class ResolveHavingConditionRule implements Rule<LogicalPlan> {
    private final Catalog catalog;

    @Override
    public Optional<LogicalPlan> apply(LogicalPlan node) {
        return node.transformUp(n -> {
            if (n instanceof Filter && ((Filter) n).getChild() instanceof Aggregate) {
                Filter filter = (Filter) n;
                Aggregate aggregate = (Aggregate) filter.getChild();
                BooleanExpression condition = filter.getCondition();

                if (aggregate.getExpressions().stream().allMatch(Expression::isResolved) && !condition.isResolved()) {
                    AttributeSet attributes = new AttributeSet(aggregate.getChild().outputs(catalog).toArray(new Attribute[0]));
                    Optional<Expression> resolvedCondition = condition.transformUp(new ResolveExpressionAttributeRule(attributes));
                    // find all AggExpressions in resolvedCondition
                    if (resolvedCondition.isPresent()) {
                        Pair<Optional<Expression>, Set<AggExpression>> aggExpressions = extractAggExpressions(aggregate, resolvedCondition.get());
                        Optional<Expression> furtherResolvedCondition = aggExpressions.getLeft();
                        Set<AggExpression> aggregatorsFromCondition = aggExpressions.getRight();
                        if (furtherResolvedCondition.isPresent()) {
                            BooleanExpression newCondition = (BooleanExpression) furtherResolvedCondition.get();
                            List<Expression> newAggregators = new ArrayList<>(aggregate.getAggregators());
                            newAggregators.addAll(aggregatorsFromCondition);
                            Aggregate newAggregate = aggregate.withAggregators(newAggregators);
                            LogicalPlan newFilter = filter.withExpressions(Collections.singletonList(newCondition)).withChildren(Collections.singletonList(newAggregate));
                            // TODO: use ResolvedAttribute after deprecating ordinal field from ResolvedAttribute
                            List<Expression> projections = aggregate.getExpressions().stream().map(e -> new UnResolvedAttribute(null, e.toString(), e.getId())).collect(Collectors.toList());
                            return Optional.of(new Project(projections, newFilter));
                        }
                    }
                    return Optional.empty();
                } else {
                    return Optional.empty();
                }
            } else {
                return Optional.empty();
            }
        });
    }

    /**
     * Extract possibly new {@link AggExpression}s from input expression.
     *
     * @param aggregate against which to check if the returned {@link AggExpression} is actually new.
     * @param input     input {@link Expression}.
     * @return A optionally new expression if there is any new {@link AggExpression}, and a set of
     * the new {@link AggExpression}s.
     */
    private Pair<Optional<Expression>, Set<AggExpression>> extractAggExpressions(Aggregate aggregate, Expression input) {
        final Set<AggExpression> newAggExpressions = new HashSet<>();
        Optional<Expression> newCondition = input.transformUp(e -> {
            if (e instanceof AggExpression) {
                AggExpression agg = (AggExpression) e;
                Optional<AggExpression> existingAgg = findDuplicate(aggregate, agg);
                if (existingAgg.isPresent()) {
                    agg = existingAgg.get();
                }
                newAggExpressions.add(agg);
                // TODO: use ResolvedAttribute after deprecating ordinal field from ResolvedAttribute
                return Optional.of(new UnResolvedAttribute(null, agg.toString(), agg.getId()));
            } else {
                return Optional.empty();
            }
        });

        return Pair.of(newCondition, newAggExpressions);
    }

    /**
     * Find for the input a duplicated {@link AggExpression} from the aggregate if any.
     *
     * @param aggregate against which to check if the input is duplicated.
     * @param input     input {@link AggExpression}
     * @return A duplicated {@link AggExpression} if any
     */
    private Optional<AggExpression> findDuplicate(Aggregate aggregate, AggExpression input) {
        return aggregate.getAggregators().stream()
                .filter(e -> e instanceof AggExpression && e.toString().equals(input.toString()))
                .map(e -> (AggExpression) e)
                .findFirst();
    }
}
