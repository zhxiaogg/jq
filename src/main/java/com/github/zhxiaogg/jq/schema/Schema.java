package com.github.zhxiaogg.jq.schema;

import com.github.zhxiaogg.jq.plan.exec.ObjectReader;
import com.github.zhxiaogg.jq.plan.exec.Record;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Data
@ToString
@EqualsAndHashCode
public class Schema {
    private final SchemaName name;
    private final List<Attribute> attributes;

    public Schema(SchemaName name, List<Attribute> attributes) {
        this.name = name;
        this.attributes = attributes;
    }

    public ObjectReader reader() {
        return new ObjectReader() {
            @Override
            public Record read(Object data) {
                List<Object> values = new ArrayList<>(attributes.size());
                int i = 0;
                for (Attribute attribute : attributes) {
                    try {
                        Field f = attribute.getField();
                        f.setAccessible(true);
                        Object value = f.get(data);
                        values.add(value);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                return Record.create(values);
            }
        };
    }
}
