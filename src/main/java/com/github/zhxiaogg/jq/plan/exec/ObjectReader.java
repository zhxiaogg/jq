package com.github.zhxiaogg.jq.plan.exec;

import com.github.zhxiaogg.jq.plan.exprs.ResolvedAttribute;

import java.util.List;

public interface ObjectReader {

    Record read(Object data);

    List<ResolvedAttribute> getAttributes();
}
