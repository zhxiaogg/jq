package com.github.zhxiaogg.jq.plan.exec;

import com.github.zhxiaogg.jq.plan.exprs.Expression;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class MutableProjection implements Projection {
    private final List<Expression> expressions;

    @Override
    public Record apply(Record input) {
        List<Object> values = expressions.stream().map(e -> e.evaluate(input)).collect(Collectors.toList());
        return Record.create(values);
    }

    public void apply(MutableRecord target, JoinRecord input) {
        List<Object> values = expressions.stream().map(e -> e.evaluate(input)).collect(Collectors.toList());
        target.setValues(values);
    }
}
