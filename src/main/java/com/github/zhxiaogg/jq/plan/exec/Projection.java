package com.github.zhxiaogg.jq.plan.exec;

import com.github.zhxiaogg.jq.analyzer.rules.ResolveExpressionAttributeRule;
import com.github.zhxiaogg.jq.plan.exprs.Expression;

import java.util.List;
import java.util.stream.Collectors;

@FunctionalInterface
public interface Projection {

    Record apply(Record input);

    static Projection create(List<Expression> expressions, AttributeSet input) {
        List<Expression> resolvedExpressions = resolveExpressions(expressions, input);

        return new Projection() {
            @Override
            public Record apply(Record input) {
                List<Object> values = resolvedExpressions.stream().map(e -> e.evaluate(input)).collect(Collectors.toList());
                return Record.create(values);
            }
        };
    }

    static MutableProjection createMutable(List<Expression> expressions, AttributeSet input) {
        List<Expression> resolvedExpressions = resolveExpressions(expressions, input);

        return new MutableProjection(resolvedExpressions);
    }

    static List<Expression> resolveExpressions(List<Expression> expressions, AttributeSet input) {
        ResolveExpressionAttributeRule rule = new ResolveExpressionAttributeRule(input, false);
        return expressions.stream().map(expr -> expr.transformUp(rule).orElse(expr)).collect(Collectors.toList());
    }
}
