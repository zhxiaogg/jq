package com.github.zhxiaogg.jq.schema;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode
@ToString
public class Attribute {
    private final String name;
    private final DataType dataType;

    public Attribute(String name, DataType dataType) {
        this.name = name;
        this.dataType = dataType;
    }
}
