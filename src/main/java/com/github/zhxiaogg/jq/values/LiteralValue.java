package com.github.zhxiaogg.jq.values;

import com.github.zhxiaogg.jq.schema.DataType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode
public class LiteralValue implements Value {
    private final Object value;
    private final DataType dataType;

    public Object getValue() {
        if (dataType.equals(DataType.Int)) {
            return ((Number) value).longValue();
        } else if (dataType.equals(DataType.Float)) {
            return ((Number) value).doubleValue();
        } else {
            return value;
        }
    }

    @Override
    public boolean isAggregator() {
        return false;
    }

    @Override
    public boolean isLiteral() {
        return true;
    }
}
