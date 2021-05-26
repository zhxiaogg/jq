package com.github.zhxiaogg.jq.schema;

public interface DataTypeSupport {
    boolean canCastTo(DataType dataType);

    Object castTo(DataType dataType, Object value);
}
