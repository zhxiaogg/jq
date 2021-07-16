package com.github.zhxiaogg.jq.schema;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Field;
import java.util.UUID;

@Data
@RequiredArgsConstructor
public class Attribute {
    private final String id;
    private final String name;
    private final DataType dataType;
    // TODO: remove this
    private final Field field;

    public Attribute(String name, DataType dataType, Field field) {
        this(UUID.randomUUID().toString(), name, dataType, field);
    }
}
