package com.github.zhxiaogg.jq.schema;

import com.github.zhxiaogg.jq.plan.exec.AttributeSet;
import lombok.Data;

@Data
public class Schema {
    private final SchemaName name;
    private final RecordReader reader;

    public AttributeSet getAttributes() {
        return reader.getAttributes();
    }
}
