package com.github.zhxiaogg.jq.values;

public abstract class AggValue implements Value {
    public boolean isAggregator() {
        return true;
    }

    public boolean isLiteral() {
        return false;
    }

    public abstract AggValue merge(AggValue aggregator);

    protected void verifySameType(AggValue aggregator) {
        if (!(aggregator.getClass() == this.getClass()))
            throw new IllegalArgumentException("cannot merge with " + aggregator.getClass());
        if (aggregator.getDataType() != this.getDataType())
            throw new IllegalArgumentException(String.format("merge with different data type, %s vs. %s", this.getDataType(), aggregator.getDataType()));
    }
}
