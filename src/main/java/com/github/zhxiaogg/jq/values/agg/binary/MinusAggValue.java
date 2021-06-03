package com.github.zhxiaogg.jq.values.agg.binary;

import com.github.zhxiaogg.jq.schema.DataType;
import com.github.zhxiaogg.jq.utils.MathUtils;
import com.github.zhxiaogg.jq.values.AggValue;

public class MinusAggValue extends BinaryAggValue {
    public MinusAggValue(AggValue left, AggValue right) {
        super(left, right);
    }

    @Override
    public AggValue merge(AggValue aggregator) {
        verifySameType(aggregator);
        PlusAggValue other = (PlusAggValue) aggregator;
        return new MinusAggValue(other.left.merge(left), other.right.merge(right));
    }

    @Override
    public Object applyWithDataType(DataType dataType, Object l, Object r) {
        switch (dataType) {
            case Float:
                return MathUtils.doubleMinus(l, r);
            case Int:
                return MathUtils.longMinus(l, r);
            default:
                throw new IllegalStateException("cannot sum on " + dataType);
        }
    }
}
