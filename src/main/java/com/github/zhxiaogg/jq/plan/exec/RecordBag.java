package com.github.zhxiaogg.jq.plan.exec;

import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class RecordBag {
    private final List<Record> records;

    public static RecordBag single(Record record) {
        return new RecordBag(Collections.singletonList(record));
    }

    public static RecordBag empty() {
        return new RecordBag(Collections.emptyList());
    }

    public static RecordBag of(List<Record> records) {
        return new RecordBag(records);
    }
}
