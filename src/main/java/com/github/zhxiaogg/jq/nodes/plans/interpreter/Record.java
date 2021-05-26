package com.github.zhxiaogg.jq.nodes.plans.interpreter;

import com.github.zhxiaogg.jq.values.Value;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@EqualsAndHashCode
public class Record {
    private final List<Value> values;

    public static Record create(List<Value> values) {
        return new Record(values);
    }
}
