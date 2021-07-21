package com.github.zhxiaogg.jq.plan.exec;

import com.github.zhxiaogg.jq.schema.DataType;
import lombok.Data;

@Data
public class Value {
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
}
