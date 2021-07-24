package com.github.zhxiaogg.jq.schema;

import com.github.zhxiaogg.jq.plan.exec.AttributeSet;
import lombok.Data;

@Data
public class Schema {
    private final String[] names;
    private final AttributeSet attributes;
    private final RecordReader reader;

    public Schema(String[] names, AttributeSet attributes, RecordReader reader) {
        this.names = names;
        this.attributes = attributes;
        this.reader = reader;
    }

    public AttributeSet getAttributes() {
        return attributes;
    }
}
