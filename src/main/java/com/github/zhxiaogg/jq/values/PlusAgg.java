package com.github.zhxiaogg.jq.values;

import com.github.zhxiaogg.jq.utils.Requires;
import com.github.zhxiaogg.jq.schema.DataType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;


@Data
@ToString
@EqualsAndHashCode(callSuper = false)
public class PlusAgg extends Aggregator {
    private final Aggregator left;
    private final Aggregator right;

    public PlusAgg(Aggregator left, Aggregator right) {
        Requires.require(left.getDataType() == right.getDataType() || left.getDataType().canCastTo(right.getDataType()) || right.getDataType().canCastTo(left.getDataType()), "");
        this.left = left;
        this.right = right;
    }


    @Override
    public DataType getDataType() {
        return (left.getDataType().compareTo(right.getDataType()) <= 0) ? left.getDataType() : right.getDataType();
    }

    @Override
    public Aggregator merge(Aggregator aggregator) {
        verifySameType(aggregator);
        PlusAgg other = (PlusAgg) aggregator;
        return new PlusAgg(other.left.merge(left), other.right.merge(right));
    }

    @Override
    public Object getValue() {
        if (left.getDataType() == right.getDataType()) {
            return sum(left.getValue(), right.getValue(), left.getDataType());
        } else if (left.getDataType().canCastTo(right.getDataType())) {
            return castAndSum(left, right);
        } else if (right.getDataType().canCastTo(left.getDataType())) {
            return castAndSum(right, left);
        } else {
            throw new IllegalStateException("wtf");
        }
    }

    private Object castAndSum(Aggregator left, Aggregator right) {
        Object leftValue = left.getDataType().castTo(right.getDataType(), left.getValue());
        return sum(leftValue, right.getValue(), right.getDataType());
    }

    private Object sum(Object leftValue, Object rightValue, DataType dataType) {
        switch (dataType) {
            case Float:
                return MathUtils.doubleSum(leftValue, rightValue);
            case Int:
                return MathUtils.longSum(leftValue, rightValue);
            default:
                throw new IllegalStateException("cannot sum on " + dataType);
        }
    }
}
