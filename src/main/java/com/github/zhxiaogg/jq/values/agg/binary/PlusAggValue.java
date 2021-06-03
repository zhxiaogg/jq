package com.github.zhxiaogg.jq.values.agg.binary;

import com.github.zhxiaogg.jq.schema.DataType;
import com.github.zhxiaogg.jq.utils.MathUtils;
import com.github.zhxiaogg.jq.values.AggValue;
import lombok.EqualsAndHashCode;
import lombok.ToString;


@ToString
@EqualsAndHashCode(callSuper = false)
public class PlusAggValue extends BinaryAggValue {

    public PlusAggValue(AggValue left, AggValue right) {
        super(left, right);
    }

    @Override
    public AggValue merge(AggValue aggregator) {
        verifySameType(aggregator);
        PlusAggValue other = (PlusAggValue) aggregator;
        return new PlusAggValue(other.left.merge(left), other.right.merge(right));
    }

    @Override
    public Object applyWithDataType(DataType dataType, Object l, Object r) {
        switch (dataType) {
            case Float:
                return MathUtils.doubleSum(l, r);
            case Int:
                return MathUtils.longSum(l, r);
            default:
                throw new IllegalStateException("cannot sum on " + dataType);
        }
    }
}
