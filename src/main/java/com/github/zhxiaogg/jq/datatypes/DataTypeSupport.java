package com.github.zhxiaogg.jq.datatypes;

public interface DataTypeSupport {
    boolean canCastTo(DataType dataType);

    Object castTo(DataType dataType, Object value);
}
