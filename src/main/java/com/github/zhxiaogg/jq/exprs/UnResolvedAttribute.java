package com.github.zhxiaogg.jq.exprs;

import com.github.zhxiaogg.jq.plans.interpreter.Record;
import com.github.zhxiaogg.jq.values.Value;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode
public class UnResolvedAttribute implements Expression {
    private final String name;

    public UnResolvedAttribute(String name) {
        this.name = name;
    }

    @Override
    public Value eval(Record record) {
        throw new UnsupportedOperationException("");
    }
}
