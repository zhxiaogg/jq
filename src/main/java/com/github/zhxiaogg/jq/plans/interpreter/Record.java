package com.github.zhxiaogg.jq.plans.interpreter;

import com.github.zhxiaogg.jq.schema.Schema;
import com.github.zhxiaogg.jq.values.Value;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;
import java.util.Optional;

@Data
@ToString
@EqualsAndHashCode
public class Record {
    private final List<Value> values;

    public static Record create(List<Value> values) {
        return new Record(values);
    }
}
