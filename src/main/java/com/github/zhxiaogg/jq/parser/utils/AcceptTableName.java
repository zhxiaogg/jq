package com.github.zhxiaogg.jq.parser.utils;

import com.github.zhxiaogg.jq.ast.TableName;

interface AcceptTableName {
    void accept(TableName tableName);
}
