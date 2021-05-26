package com.github.zhxiaogg.jq;

import com.github.zhxiaogg.jq.plans.interpreter.Record;
import com.github.zhxiaogg.jq.plans.interpreter.RecordBag;
import com.github.zhxiaogg.jq.schema.Attribute;
import com.github.zhxiaogg.jq.schema.DataType;
import com.github.zhxiaogg.jq.schema.Schema;
import com.github.zhxiaogg.jq.schema.SchemaName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.lang.reflect.Field;
import java.util.*;

@Data
@EqualsAndHashCode
@ToString
public class Relation {
    private final Schema schema;
    private final List<Record> records = new LinkedList<>();

    public Relation(Schema schema) {
        this.schema = schema;
    }

    public static Relation create(String name, Class<?> clazz) {
        List<Attribute> attributes = new ArrayList<>();
        for (Field field : clazz.getFields()) {
            com.github.zhxiaogg.jq.annotations.Field annotation = field.getAnnotation(com.github.zhxiaogg.jq.annotations.Field.class);
            if (annotation == null) continue;
            final String fieldName = Optional.of(annotation.name()).orElseGet(field::getName);
            final DataType datatype;
            if (int.class.equals(field.getType()) || Integer.class.equals(field.getType()) || Long.class.equals(field.getType()) || long.class.equals(field.getType())) {
                datatype = DataType.Int;
            } else if (String.class.equals(field.getType())) {
                datatype = DataType.String;
            } else if (Boolean.class.equals(field.getType()) || boolean.class.equals(field.getType())) {
                datatype = DataType.Boolean;
            } else {
                throw new IllegalArgumentException("unsupported field type: " + field.getType().getCanonicalName());
            }
            attributes.add(new Attribute(name, datatype));
        }
        return new Relation(new Schema(new SchemaName(clazz, name), attributes));
    }

    public RecordBag records() {
        return RecordBag.of(Collections.unmodifiableList(records));
    }

    public void add(Object data) {
        if (data.getClass() != schema.getName().getClazz()) {
            throw new IllegalArgumentException("expected " + schema.getName().getClazz());
        }
        Record record = schema.reader().read(data);
        records.add(record);
    }
}
