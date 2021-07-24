package com.github.zhxiaogg.jq.analyzer;

import com.github.zhxiaogg.jq.plan.exprs.Expression;
import com.github.zhxiaogg.jq.plan.exprs.aggregators.AggExpression;
import com.github.zhxiaogg.jq.utils.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// TODO: replacing for-loop search with a HashMap
public class AggregatorUtil {
    /**
     * Transform input expressions by extracting out {@link AggExpression}s.
     *
     * @param expressions input expressions
     * @return list of {@link AggExpression}s and list of transformed {@link Expression}s.
     */
    public static Pair<List<AggExpression>, List<Expression>> extractAggregators(List<Expression> expressions) {
        List<Expression> resultExpressions = new ArrayList<>(expressions.size());
        List<AggExpression> aggExpressions = new ArrayList<>();
        for (Expression expression : expressions) {
            Pair<Optional<Expression>, List<AggExpression>> resolved = tryExtractAggExpression(expression, aggExpressions);
            if (resolved.getLeft().isPresent()) {
                resultExpressions.add(resolved.getLeft().get());
            } else {
                throw new IllegalArgumentException("cannot resolve aggregators!");
            }
        }
        return Pair.of(aggExpressions, resultExpressions);
    }

    /**
     * Transform the input expression by extracting and replacing {@link AggExpression}s.
     *
     * @param expression input expression
     * @param existings  current existing {@link AggExpression} that can be used to transform the input expression.
     * @return return the optionally resolved expression and new {@link AggExpression}s that not present in existings.
     */
    public static Pair<Optional<Expression>, List<AggExpression>> tryExtractAggExpression(Expression expression, List<AggExpression> existings) {
        List<AggExpression> newAggExpressions = new ArrayList<>();
        Optional<Expression> optionalResolved = expression.transformDown(e -> {
            if (e instanceof AggExpression) {
                Optional<AggExpression> existing = existings.stream().filter(agg -> agg.semanticEqual(e)).findFirst();
                if (!existing.isPresent()) {
                    existings.add((AggExpression) e);
                    newAggExpressions.add((AggExpression) e);
                }
                AggExpression expr = existing.orElse((AggExpression) e);
                return Optional.of(expr.toAttribute());
            } else {
                return Optional.empty();
            }
        });
        return Pair.of(optionalResolved, newAggExpressions);
    }
}
