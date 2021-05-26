package com.github.zhxiaogg.jq.schema;

import com.github.zhxiaogg.jq.nodes.plans.interpreter.ObjectReader;
import com.github.zhxiaogg.jq.nodes.plans.interpreter.Record;
import com.github.zhxiaogg.jq.values.LiteralValue;
import com.github.zhxiaogg.jq.values.Value;
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
                List<Value> values = new ArrayList<>(attributes.size());
                int i = 0;
                for (Attribute attribute : attributes) {
                    try {
                        Field f = attribute.getField();
                        f.setAccessible(true);
                        Object value = f.get(data);
                        values.add(new LiteralValue(value, attribute.getDataType()));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                return Record.create(values);
            }
        };
    }
}
