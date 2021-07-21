package com.github.zhxiaogg.jq.plan.exec;

import com.github.zhxiaogg.jq.plan.exprs.Expression;
import com.github.zhxiaogg.jq.values.LiteralValue;
import com.github.zhxiaogg.jq.values.Value;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class MutableProjection implements Projection {
    private final List<Expression> expressions;

    @Override
    public Record apply(Record input) {
        List<Value> values = expressions.stream().map(e -> new LiteralValue(e.evaluate(input), e.getDataType())).collect(Collectors.toList());
        return Record.create(values);
    }

    public void apply(MutableRecord target, JoinRecord input) {
        List<Value> values = expressions.stream().map(e -> new LiteralValue(e.evaluate(input), e.getDataType())).collect(Collectors.toList());
        target.setValues(values);
    }
}
