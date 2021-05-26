package com.github.zhxiaogg.jq.schema;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.lang.reflect.Field;

@Data
@EqualsAndHashCode
@ToString
public class Attribute {
    private final String name;
    private final DataType dataType;
    // TODO: remove this?
    private final Field field;
}
