package com.github.zhxiaogg.jq.plan.exprs;

import com.github.zhxiaogg.jq.plan.exec.Record;
import com.github.zhxiaogg.jq.schema.DataType;
import com.github.zhxiaogg.jq.values.LiteralValue;
import com.github.zhxiaogg.jq.values.Value;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.Instant;
import java.util.UUID;

@Data
public class Literal implements LeafExprNode {
    private final Object value;
    private final DataType dataType;
    private final String id = UUID.randomUUID().toString();

    public static Expression create(int value) {
        return new Literal(value, DataType.Int);
    }

    public static Expression create(Instant value) {
        return new Literal(value, DataType.DateTime);
    }

    @Override
    public boolean isResolved() {
        return true;
    }

    @Override
    public Value eval(Record record) {
        return new LiteralValue(value, dataType);
    }

    @Override
    public String toString() {
        // TODO: add a thread local name idx?
        return String.format("Literal(%s)", value);
    }
}
