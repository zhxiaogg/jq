package com.github.zhxiaogg.jq.plan.exec;

import com.github.zhxiaogg.jq.analyzer.rules.ResolveExpressionAttributeRule;
import com.github.zhxiaogg.jq.plan.exprs.Expression;
import com.github.zhxiaogg.jq.values.Value;

import java.util.List;
import java.util.stream.Collectors;

import static com.github.zhxiaogg.jq.utils.Requires.require;

@FunctionalInterface
public interface Projection {

    Record apply(Record input);

    static Projection create(List<Expression> expressions, AttributeSet input) {
        require(expressions.stream().allMatch(Expression::isResolved), "expression is not resolved!");
        List<Expression> resolvedExpressions = resolveExpressions(expressions, input);

        return new Projection() {
            @Override
            public Record apply(Record input) {
                List<Value> values = resolvedExpressions.stream().map(e -> e.eval(input)).collect(Collectors.toList());
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
