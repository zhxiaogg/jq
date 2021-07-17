package com.github.zhxiaogg.jq.plan.exprs.literals;

import com.github.zhxiaogg.jq.schema.DataType;
import lombok.Data;

import java.util.UUID;

@Data
public class LiteralImpl implements Literal {
    private final Object value;
    private final DataType dataType;
    private final String id = UUID.randomUUID().toString();

    @Override
    public String toString() {
        return String.format("Literal(%s)", value);
    }
}
