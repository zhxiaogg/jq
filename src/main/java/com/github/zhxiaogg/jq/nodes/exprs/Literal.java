package com.github.zhxiaogg.jq.nodes.exprs;

import com.github.zhxiaogg.jq.nodes.logical.interpreter.Record;
import com.github.zhxiaogg.jq.schema.DataType;
import com.github.zhxiaogg.jq.values.LiteralValue;
import com.github.zhxiaogg.jq.values.Value;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.Instant;

@Data
@ToString
@EqualsAndHashCode
public class Literal implements LeafExprNode {
    private final Object value;
    private final DataType dataType;

    public static Expression create(int value) {
        return new Literal(value, DataType.Int);
    }

    public static Expression create(Instant value) {
        return new Literal(value, DataType.DateTime);
    }

    @Override
    public Value eval(Record record) {
        return new LiteralValue(value, dataType);
    }

    @Override
    public String getDisplayName() {
        // TODO: add a thread local name idx?
        return "Literal_";
    }
}
