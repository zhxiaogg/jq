package com.github.zhxiaogg.jq.plan.exec;

import com.github.zhxiaogg.jq.values.Value;
import lombok.Data;

import java.util.List;

@Data
public class SimpleRecord implements Record {
    private final List<Value> values;

    public Value indexOf(int ordinal) {
        return values.get(ordinal);
    }

    @Override
    public int size() {
        return values.size();
    }

    @Override
    public MutableRecord toMutable() {
        return new MutableRecord(values);
    }
}
