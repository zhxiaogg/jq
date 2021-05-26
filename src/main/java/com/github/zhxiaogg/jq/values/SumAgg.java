package com.github.zhxiaogg.jq.values;

import com.github.zhxiaogg.jq.schema.DataType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode(callSuper = false)
public class SumAgg extends Aggregator {
    private final Object value;
    private final DataType dataType;

    public static SumAgg from(LiteralValue value) {
        return new SumAgg(value.getValue(), value.getDataType());
    }

    @Override
    public Aggregator merge(Aggregator aggregator) {
        verifySameType(aggregator);
        Object leftValue = this.getValue();
        Object rightValue = aggregator.getValue();
        switch (this.dataType) {
            case Float:
                double f = MathUtils.doubleSum(leftValue, rightValue);
                return new SumAgg(f, this.dataType);
            case Int:
                long l = MathUtils.longSum(leftValue, rightValue);
                return new SumAgg(l, this.dataType);
            default:
                throw new IllegalStateException("Constructing SumValue from " + this.dataType);
        }
    }

}
