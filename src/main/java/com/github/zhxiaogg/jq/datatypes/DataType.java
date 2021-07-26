package com.github.zhxiaogg.jq.datatypes;

import java.time.Instant;

public enum DataType implements DataTypeSupport, Comparable<DataType> {
    UnKnown {
        @Override
        public Object castTo(DataType dataType, Object value) {
            throw new UnsupportedOperationException("");
        }

        @Override
        public boolean canCastTo(DataType dataType) {
            return false;
        }

        @Override
        public boolean isPrimitive() {
            return false;
        }
    },
    Any {
        @Override
        public Object castTo(DataType dataType, Object value) {
            if (value == null) return null;
            DataType actualDataType = getDataType(value.getClass());
            if (dataType.equals(actualDataType)) {
                return value;
            } else if (actualDataType.canCastTo(dataType)) {
                return actualDataType.castTo(dataType, value);
            } else {
                throw new UnsupportedOperationException("cannot cast to " + dataType);
            }
        }

        @Override
        public boolean canCastTo(DataType dataType) {
            return true;
        }

        @Override
        public boolean isPrimitive() {
            return false;
        }
    },
    Float {
        @Override
        public Object castTo(DataType dataType, Object value) {
            throw new UnsupportedOperationException("");
        }

        @Override
        public boolean canCastTo(DataType dataType) {
            return false;
        }

        @Override
        public boolean isPrimitive() {
            return true;
        }
    },
    Int {
        @Override
        public Object castTo(DataType dataType, Object value) {
            if (dataType == Float) {
                return ((Number) value).doubleValue();
            } else {
                throw new UnsupportedOperationException("cannot cast Int to " + dataType);
            }
        }

        @Override
        public boolean canCastTo(DataType dataType) {
            return dataType.equals(Float);
        }

        @Override
        public boolean isPrimitive() {
            return true;
        }
    },
    String {
        @Override
        public Object castTo(DataType dataType, Object value) {
            if (dataType.equals(DateTime)) {
                return Instant.parse(value.toString());
            } else {
                throw new UnsupportedOperationException("");
            }
        }

        @Override
        public boolean canCastTo(DataType dataType) {
            return dataType.equals(DateTime);
        }

        @Override
        public boolean isPrimitive() {
            return true;
        }
    },
    Boolean {
        @Override
        public Object castTo(DataType dataType, Object value) {
            throw new UnsupportedOperationException("");
        }

        @Override
        public boolean canCastTo(DataType dataType) {
            return false;
        }

        @Override
        public boolean isPrimitive() {
            return true;
        }
    },
    DateTime {
        @Override
        public Object castTo(DataType dataType, Object value) {
            throw new UnsupportedOperationException("");
        }

        @Override
        public boolean canCastTo(DataType dataType) {
            return false;
        }

        @Override
        public boolean isPrimitive() {
            return true;
        }
    },
    Struct {
        @Override
        public boolean canCastTo(DataType dataType) {
            return false;
        }

        @Override
        public Object castTo(DataType dataType, Object value) {
            throw new UnsupportedOperationException("");
        }

        @Override
        public boolean isPrimitive() {
            return false;
        }
    };

    public static DataType getDataType(Class<?> fieldType) {
        DataType dataType;
        if (int.class.equals(fieldType) || Integer.class.equals(fieldType) || Long.class.equals(fieldType) || long.class.equals(fieldType)) {
            dataType = Int;
        } else if (float.class.equals(fieldType) || Float.class.equals(fieldType) || double.class.equals(fieldType) || Double.class.equals(fieldType)) {
            dataType = Float;
        } else if (String.class.equals(fieldType)) {
            dataType = String;
        } else if (Boolean.class.equals(fieldType) || boolean.class.equals(fieldType)) {
            dataType = Boolean;
        } else if (Instant.class.equals(fieldType)) {
            dataType = DateTime;
        } else {
            // treat the field as DataType.Struct
            dataType = Struct;
        }
        return dataType;
    }
}

