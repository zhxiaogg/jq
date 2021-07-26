package com.github.zhxiaogg.jq.plan.exprs.booleans;

import com.github.zhxiaogg.jq.datatypes.DataType;
import com.github.zhxiaogg.jq.utils.BinaryValueOp;

import java.time.Instant;

public enum CompareOp implements CompareOpImpl, BinaryValueOp<Boolean> {
    GT {
        @Override
        public <T extends Comparable<T>> boolean compareImpl(T left, T right) {
            return left.compareTo(right) > 0;
        }
    },
    GTE {
        @Override
        public <T extends Comparable<T>> boolean compareImpl(T left, T right) {
            return left.compareTo(right) >= 0;
        }
    },
    LT {
        @Override
        public <T extends Comparable<T>> boolean compareImpl(T left, T right) {
            return left.compareTo(right) < 0;
        }
    },
    LTE {
        @Override
        public <T extends Comparable<T>> boolean compareImpl(T left, T right) {
            return left.compareTo(right) <= 0;
        }
    },
    EQ {
        @Override
        public <T extends Comparable<T>> boolean compareImpl(T left, T right) {
            return left.compareTo(right) == 0;
        }
    },
    NE {
        @Override
        public <T extends Comparable<T>> boolean compareImpl(T left, T right) {
            return left.compareTo(right) != 0;
        }
    },
    LIKE {
        @Override
        public <T extends Comparable<T>> boolean compareImpl(T left, T right) {
            throw new UnsupportedOperationException("like is not supported!");
        }
    };


    public Boolean applyWithDataType(DataType dataType, Object l, Object r) {
        switch (dataType) {
            case Float:
                return this.compareImpl(((Number) l).doubleValue(), ((Number) r).doubleValue());
            case Int:
                return this.compareImpl(((Number) l).longValue(), ((Number) r).longValue());
            case DateTime:
                return this.compareImpl((Instant) l, (Instant) r);
            case String:
                return this.compareImpl((String) l, (String) r);
            case Boolean:
                return this.compareImpl((Boolean) l, (Boolean) r);
        }
        return false;
    }


}
