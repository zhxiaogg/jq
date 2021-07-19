package com.github.zhxiaogg.jq.analyzer;

import com.github.zhxiaogg.jq.plan.exprs.Expression;
import com.github.zhxiaogg.jq.plan.exprs.ResolvedAttribute;
import com.github.zhxiaogg.jq.plan.exprs.aggregators.AggExpression;
import com.github.zhxiaogg.jq.utils.Pair;

import java.util.*;

public class AggregatorUtil {
    public static Pair<Map<String, AggExpression>, List<Expression>> extractAggregators(List<Expression> expressions) {
        Map<String, AggExpression> aggExpressions = new HashMap<>();
        List<Expression> resultExpressions = new ArrayList<>(expressions.size());
        for (Expression expression : expressions) {
            Pair<Optional<Expression>, Map<String, AggExpression>> resolved = tryExtractAggExpression(expression, aggExpressions);
            if (resolved.getLeft().isPresent()) {
                resultExpressions.add(resolved.getLeft().get());
            } else {
                throw new IllegalStateException("cannot resolve aggregators!");
            }
        }
        return Pair.of(aggExpressions, resultExpressions);
    }

    /**
     * Added to the existing map if the possible new {@link AggExpression} does not existed.
     *
     * @param expression
     * @param existing
     * @return return the optionally resolved expression.
     */
    public static Pair<Optional<Expression>, Map<String, AggExpression>> tryExtractAggExpression(Expression expression, Map<String, AggExpression> existing) {
        Map<String, AggExpression> map = new HashMap<>();
        Optional<Expression> optionalResolved = expression.transformDown(e -> {
            if (e instanceof AggExpression) {
                String key = e.toString();
                map.computeIfAbsent(key, k -> (AggExpression) e);
                AggExpression expr = existing.computeIfAbsent(key, k -> (AggExpression) e);
                return Optional.of(new ResolvedAttribute(expr.getId(), expr.toString(), expr.getDataType(), -1));
            } else {
                return Optional.empty();
            }
        });
        return Pair.of(optionalResolved, map);
    }
}
