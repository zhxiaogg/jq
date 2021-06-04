package com.github.zhxiaogg.jq.values.agg.binary;

import com.github.zhxiaogg.jq.schema.DataType;
import com.github.zhxiaogg.jq.utils.MathUtils;
import com.github.zhxiaogg.jq.values.AggValue;

public class ProductAggValue extends BinaryAggValue {
    public ProductAggValue(AggValue left, AggValue right) {
        super(left, right);
    }

    @Override
    public AggValue merge(AggValue aggregator) {
        verifySameType(aggregator);
        ProductAggValue other = (ProductAggValue) aggregator;
        return new ProductAggValue(other.left.merge(left), other.right.merge(right));
    }

    @Override
    public Object applyWithDataType(DataType dataType, Object l, Object r) {
        switch (dataType) {
            case Float:
                return MathUtils.doubleProduct(l, r);
            case Int:
                return MathUtils.longProduct(l, r);
            default:
                throw new IllegalStateException("cannot run product on " + dataType);
        }
    }
}
