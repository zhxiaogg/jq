package com.github.zhxiaogg.jq.schema;

import com.github.zhxiaogg.jq.plan.exec.Record;

public interface RecordReader {
    Record read(Object data);
}
