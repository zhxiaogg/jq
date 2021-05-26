package com.github.zhxiaogg.jq.exprs;

import com.github.zhxiaogg.jq.plans.interpreter.Record;
import com.github.zhxiaogg.jq.values.Value;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode
public class Alias implements Expression {
    private final Expression inner;
    private final String name;

    @Override
    public Value eval(Record record) {
        return inner.eval(record);
    }
}
