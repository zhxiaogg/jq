package com.github.zhxiaogg.jq.catalog;

import com.github.zhxiaogg.jq.plan.exec.SimpleAttributeSet;
import lombok.Data;

@Data
public class Schema {
    private final String[] names;
    private final SimpleAttributeSet attributes;
    private final RecordReader reader;

    public Schema(String[] names, SimpleAttributeSet attributes, RecordReader reader) {
        this.names = names;
        this.attributes = attributes;
        this.reader = reader;
    }

    public SimpleAttributeSet getAttributes() {
        return attributes;
    }
}
