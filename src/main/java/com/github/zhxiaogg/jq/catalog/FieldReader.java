package com.github.zhxiaogg.jq.catalog;

import lombok.Data;

import java.lang.reflect.Field;

public interface FieldReader {

    Object read(Object input);

    static FieldReader create(Field field) {
        return new PrimitiveFieldReader(field);
    }

    static FieldReader create(Field field, RecordReader reader) {
        return new StructFieldReader(field, reader);
    }

    @Data
    class PrimitiveFieldReader implements FieldReader {
        private final Field field;

        @Override
        public Object read(Object input) {
            if (input == null) {
                return null;
            }
            try {
                return field.get(input);
            } catch (IllegalAccessException e) {
                throw new IllegalStateException("cannot access field " + field.getName());
            }
        }
    }

    @Data
    class StructFieldReader implements FieldReader {
        private final Field field;
        private final RecordReader reader;

        @Override
        public Object read(Object input) {
            try {
                Object struct = field.get(input);
                return reader.read(struct);
            } catch (IllegalAccessException e) {
                throw new IllegalStateException("cannot access field " + field.getName());
            }
        }
    }

}
