package com.github.zhxiaogg.jq.catalog;

import com.github.zhxiaogg.jq.datatypes.DataType;
import com.github.zhxiaogg.jq.plan.exec.AttributeSet;
import com.github.zhxiaogg.jq.plan.exec.Record;
import com.github.zhxiaogg.jq.plan.exec.RecordBag;
import com.github.zhxiaogg.jq.plan.exprs.ResolvedAttribute;
import com.github.zhxiaogg.jq.utils.Pair;
import lombok.Data;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Data
public class ObjectRelation implements Relation {
    private final Schema schema;
    private final Class<?> clazz;
    private final List<Record> records = new LinkedList<>();

    /**
     * Get {@link AttributeSet} and {@link RecordReader} for a Class.
     *
     * @param clazz
     * @return
     */
    static Pair<AttributeSet, RecordReader> resolveAttributeSet(Class<?> clazz) {
        List<FieldReader> fieldReaders = new ArrayList<>();
        List<ResolvedAttribute> attributes = new ArrayList<>();
        int idx = 0;
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            Optional<Pair<ResolvedAttribute, FieldReader>> optAttribute = resolveAttribute(idx, field);
            if (!optAttribute.isPresent()) continue;
            fieldReaders.add(optAttribute.get().getRight());
            attributes.add(optAttribute.get().getLeft());
            idx += 1;
        }
        RecordReader reader = data -> {
            List<Object> values = new ArrayList<>(attributes.size());
            for (ResolvedAttribute attribute : attributes) {
                FieldReader f = fieldReaders.get(attribute.getOrdinals()[0]);
                Object value = f.read(data);
                values.add(value);
            }
            return Record.create(values);
        };
        return Pair.of(AttributeSet.create(attributes), reader);
    }

    /**
     * Get {@link ResolvedAttribute} and {@link FieldReader} for a given {@link Field}
     *
     * @param idx   ordinal of the field in the parent {@link AttributeSet}
     * @param field
     * @return
     */
    static Optional<Pair<ResolvedAttribute, FieldReader>> resolveAttribute(int idx, Field field) {
        com.github.zhxiaogg.jq.annotations.Field annotation = field.getAnnotation(com.github.zhxiaogg.jq.annotations.Field.class);
        if (annotation == null) return Optional.empty();
        final String fieldName = Optional.of(annotation.name()).filter(s -> !s.isEmpty()).orElseGet(field::getName);
        Class<?> fieldType = field.getType();
        FieldReader fr = FieldReader.create(field);
        ResolvedAttribute attribute;
        DataType dataType = DataType.getDataType(fieldType);
        if (dataType.isPrimitive()) {
            attribute = new ResolvedAttribute(fieldName, dataType, idx);
        } else if (dataType.equals(DataType.Struct)) {
            // treat the field as DataType.Struct
            Pair<AttributeSet, RecordReader> nestedAttribute = resolveAttributeSet(fieldType);
            fr = FieldReader.create(field, nestedAttribute.getRight());
            attribute = new ResolvedAttribute(fieldName, DataType.Struct, nestedAttribute.getLeft(), idx);
        } else {
            throw new IllegalStateException(String.format("unsupported field %s with data type: %s", field.getName(), dataType));
        }

        return Optional.of(Pair.of(attribute, fr));
    }

    @Override
    public RecordBag scan() {
        ArrayList<Record> result = new ArrayList<>(records.size());
        result.addAll(records);
        return RecordBag.of(result);
    }

    @Override
    public void insert(Object data) {
        if (data.getClass() != clazz) {
            throw new IllegalArgumentException("expected " + clazz);
        }
        Record record = schema.getReader().read(data);
        records.add(record);
    }
}
