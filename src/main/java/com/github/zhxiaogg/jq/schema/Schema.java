package com.github.zhxiaogg.jq.schema;

import com.github.zhxiaogg.jq.plan.exec.ObjectReader;
import com.github.zhxiaogg.jq.plan.exprs.ResolvedAttribute;
import lombok.Data;

import java.util.List;

@Data
public class Schema {
    private final SchemaName name;
    private final ObjectReader reader;

    public List<ResolvedAttribute> getAttributes() {
        return reader.getAttributes();
    }
}
