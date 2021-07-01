package com.github.zhxiaogg.jq.parser.utils;

import com.github.zhxiaogg.jq.ast.ColumnName;

interface AcceptColumnName {
    void accept(ColumnName columnName);
}
