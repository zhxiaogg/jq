package com.github.zhxiaogg.jq.analyzer.rules;

import com.github.zhxiaogg.jq.catalog.Catalog;
import com.github.zhxiaogg.jq.analyzer.AggregatorUtil;
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
import com.github.zhxiaogg.jq.utils.Pair;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
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
                    AttributeSet attributes = aggregate.getChild().outputs(catalog);
                    Optional<Expression> resolvedCondition = condition.transformUp(new ResolveExpressionAttributeRule(attributes));
                    // find all AggExpressions in resolvedCondition
                    if (resolvedCondition.isPresent() && resolvedCondition.get().isResolved()) {
                        List<AggExpression> existings = AggregatorUtil.extractAggregators(aggregate.getAggregators()).getLeft();
                        Pair<Optional<Expression>, List<AggExpression>> extractResult = AggregatorUtil.tryExtractAggExpression(resolvedCondition.get(), existings);
                        Optional<Expression> optResolvedCondition = extractResult.getLeft();
                        List<AggExpression> newAggExpressions = extractResult.getRight();
                        if (optResolvedCondition.isPresent()) {
                            BooleanExpression newCondition = (BooleanExpression) optResolvedCondition.get();
                            List<Expression> newAggregators = new ArrayList<>(aggregate.getAggregators());
                            newAggregators.addAll(newAggExpressions);
                            Aggregate newAggregate = aggregate.withAggregators(newAggregators);
                            LogicalPlan newFilter = filter.withExpressions(Collections.singletonList(newCondition)).withChildren(Collections.singletonList(newAggregate));
                            // TODO: don't use toString here.
                            List<Expression> projections = aggregate.getExpressions().stream().map(e -> new UnResolvedAttribute(new String[]{e.toString()}, e.getId())).collect(Collectors.toList());
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
