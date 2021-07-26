package com.github.zhxiaogg.jq.catalog;

import com.github.zhxiaogg.jq.plan.exec.AttributeSet;
import lombok.Data;

@Data
public class Schema {
    private final String[] names;
    private final AttributeSet attributes;
    private final RecordReader reader;

    public static Schema empty(String[] names) {
        return new Schema(names, AttributeSet.empty(names), RecordReader.empty());
    }

    public Schema(String[] names, AttributeSet attributes, RecordReader reader) {
        this.names = names;
        this.attributes = attributes;
        this.reader = reader;
    }

    public Schema withAttributes(AttributeSet attributes) {
        return new Schema(names, attributes, reader);
    }

    public Schema withReader(RecordReader reader) {
        return new Schema(names, attributes, reader);
    }
}
