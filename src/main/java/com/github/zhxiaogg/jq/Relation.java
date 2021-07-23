package com.github.zhxiaogg.jq;

import com.github.zhxiaogg.jq.plan.exec.ObjectReader;
import com.github.zhxiaogg.jq.plan.exec.Record;
import com.github.zhxiaogg.jq.plan.exec.RecordBag;
import com.github.zhxiaogg.jq.schema.DataType;
import com.github.zhxiaogg.jq.plan.exprs.ResolvedAttribute;
import com.github.zhxiaogg.jq.schema.Schema;
import com.github.zhxiaogg.jq.schema.SchemaName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

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
        List<ResolvedAttribute> attributes = new ArrayList<>();
        List<Field> fields = new ArrayList<>();
        int idx = 0;
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            fields.add(field);
            com.github.zhxiaogg.jq.annotations.Field annotation = field.getAnnotation(com.github.zhxiaogg.jq.annotations.Field.class);
            if (annotation == null) continue;
            final String fieldName = Optional.of(annotation.name()).filter(s -> !s.isEmpty()).orElseGet(field::getName);
            final DataType datatype;
            if (int.class.equals(field.getType()) || Integer.class.equals(field.getType()) || Long.class.equals(field.getType()) || long.class.equals(field.getType())) {
                datatype = DataType.Int;
            } else if (float.class.equals(field.getType()) || Float.class.equals(field.getType()) || double.class.equals(field.getType()) || Double.class.equals(field.getType())) {
                datatype = DataType.Float;
            } else if (String.class.equals(field.getType())) {
                datatype = DataType.String;
            } else if (Boolean.class.equals(field.getType()) || boolean.class.equals(field.getType())) {
                datatype = DataType.Boolean;
            } else if (Instant.class.equals(field.getType())) {
                datatype = DataType.DateTime;
            } else {
                throw new IllegalArgumentException("unsupported field type: " + field.getType().getCanonicalName());
            }
            attributes.add(new ResolvedAttribute(fieldName, datatype, idx++));
        }
        ObjectReader reader = new ObjectReader() {
            @Override
            public Record read(Object data) {
                List<Object> values = new ArrayList<>(attributes.size());
                int i = 0;
                for (ResolvedAttribute attribute : attributes) {
                    try {
                        Field f = fields.get(attribute.getOrdinal());
                        f.setAccessible(true);
                        Object value = f.get(data);
                        values.add(value);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                return Record.create(values);
            }

            @Override
            public List<ResolvedAttribute> getAttributes() {
                return attributes;
            }
        };
        return new Relation(new Schema(new SchemaName(clazz, name), reader));
    }

    /**
     * clear records after every read
     *
     * @return
     */
    public RecordBag records() {
        ArrayList<Record> result = new ArrayList<>(records.size());
        result.addAll(records);
        RecordBag bag = RecordBag.of(result);
        // records.clear();
        return bag;
    }

    public void add(Object data) {
        if (data.getClass() != schema.getName().getClazz()) {
            throw new IllegalArgumentException("expected " + schema.getName().getClazz());
        }
        Record record = schema.getReader().read(data);
        records.add(record);
    }
}
