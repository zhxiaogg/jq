package com.github.zhxiaogg.jq.values;

import com.github.zhxiaogg.jq.schema.DataType;

public interface Value {
    boolean isAggregator();

    boolean isLiteral();

    // TODO: this could be redundant
    DataType getDataType();

    Object getValue();
}
