package com.github.zhxiaogg.jq.catalog;

import com.github.zhxiaogg.jq.plan.exec.Record;

public interface RecordReader {
    Record read(Object data);
}
