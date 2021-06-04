package com.github.zhxiaogg.jq.values.agg.binary;

import com.github.zhxiaogg.jq.schema.DataType;
import com.github.zhxiaogg.jq.utils.MathUtils;
import com.github.zhxiaogg.jq.values.AggValue;

public class DivAggValue extends BinaryAggValue {
    public DivAggValue(AggValue left, AggValue right) {
        super(left, right);
    }

    @Override
    public AggValue merge(AggValue aggregator) {
        verifySameType(aggregator);
        DivAggValue other = (DivAggValue) aggregator;
        return new DivAggValue(other.left.merge(left), other.right.merge(right));
    }

    @Override
    public Object applyWithDataType(DataType dataType, Object l, Object r) {
        switch (dataType) {
            case Float:
                return MathUtils.doubleDiv(l, r);
            case Int:
                return MathUtils.longDiv(l, r);
            default:
                throw new IllegalStateException("cannot sum on " + dataType);
        }
    }
}
