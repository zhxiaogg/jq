package com.github.zhxiaogg.jq.plan.exec;

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

        return new Projection() {
            @Override
            public Record apply(Record input) {
                List<Value> values = expressions.stream().map(e -> e.eval(input)).collect(Collectors.toList());
                return new Record(values);
            }
        };
    }
}
