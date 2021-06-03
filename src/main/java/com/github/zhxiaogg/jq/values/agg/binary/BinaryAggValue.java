package com.github.zhxiaogg.jq.values.agg.binary;

import com.github.zhxiaogg.jq.utils.BinaryValueOp;
import com.github.zhxiaogg.jq.schema.DataType;
import com.github.zhxiaogg.jq.utils.Requires;
import com.github.zhxiaogg.jq.values.AggValue;

public abstract class BinaryAggValue extends AggValue implements BinaryValueOp<Object> {
    protected final AggValue left;
    protected final AggValue right;

    protected BinaryAggValue(AggValue left, AggValue right) {
        Requires.require(left.getDataType() == right.getDataType() || left.getDataType().canCastTo(right.getDataType()) || right.getDataType().canCastTo(left.getDataType()), "");
        this.left = left;
        this.right = right;
    }

    @Override
    public Object getValue() {
        return this.apply(left, right);
    }

    @Override
    public DataType getDataType() {
        return (left.getDataType().compareTo(right.getDataType()) <= 0) ? left.getDataType() : right.getDataType();
    }

}
