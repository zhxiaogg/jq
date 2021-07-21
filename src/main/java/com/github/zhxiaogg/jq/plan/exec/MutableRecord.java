package com.github.zhxiaogg.jq.plan.exec;

import lombok.Data;

import java.util.List;

@Data
public class MutableRecord implements Record {
    private List<Object> values;

    public MutableRecord(List<Object> values) {
        this.values = values;
    }

    public Object indexOf(int ordinal) {
        return values.get(ordinal);
    }

    @Override
    public MutableRecord toMutable() {
        return this;
    }

    @Override
    public int size() {
        return values.size();
    }
}
