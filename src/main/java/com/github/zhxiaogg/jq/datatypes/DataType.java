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
    };
}

