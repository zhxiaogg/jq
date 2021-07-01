package com.github.zhxiaogg.jq.parser.utils;

import com.github.zhxiaogg.jq.ast.ResultColumn;

interface AcceptResultColumn {
    void accept(ResultColumn resultColumn);
}
