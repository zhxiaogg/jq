package com.github.zhxiaogg.jq.schema;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode
public class SchemaName {
    private final Class<?> clazz;
    private final String name;
}
