package com.github.zhxiaogg.jq.parser.utils;

import com.github.zhxiaogg.jq.ast.GroupBy;

interface AcceptGroupBy {
    void accept(GroupBy groupBy);
}
