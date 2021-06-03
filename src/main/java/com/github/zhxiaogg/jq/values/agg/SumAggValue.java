package com.github.zhxiaogg.jq.values.agg;

import com.github.zhxiaogg.jq.schema.DataType;
import com.github.zhxiaogg.jq.utils.MathUtils;
import com.github.zhxiaogg.jq.values.AggValue;
import com.github.zhxiaogg.jq.values.LiteralValue;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode(callSuper = false)
public class SumAggValue extends AggValue {
    private final Object value;
    private final DataType dataType;

    public static SumAggValue from(LiteralValue value) {
        return new SumAggValue(value.getValue(), value.getDataType());
    }

    @Override
    public AggValue merge(AggValue aggregator) {
        verifySameType(aggregator);
        Object leftValue = this.getValue();
        Object rightValue = aggregator.getValue();
        switch (this.dataType) {
            case Float:
                double f = MathUtils.doubleSum(leftValue, rightValue);
                return new SumAggValue(f, this.dataType);
            case Int:
                long l = MathUtils.longSum(leftValue, rightValue);
                return new SumAggValue(l, this.dataType);
            default:
                throw new IllegalStateException("Constructing SumValue from " + this.dataType);
        }
    }

}
