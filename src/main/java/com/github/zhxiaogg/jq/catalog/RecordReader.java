package com.github.zhxiaogg.jq.catalog;

import com.github.zhxiaogg.jq.plan.exec.Record;

import java.util.Collections;

public interface RecordReader {
    Record read(Object data);

    static RecordReader empty() {
        return data -> Record.create(Collections.emptyList());
    }
}
