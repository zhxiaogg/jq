package com.github.zhxiaogg.jq.parser.utils;

import com.github.zhxiaogg.jq.ast.TableOrSubQuery;

interface AcceptTableOrSubQuery {
    void accept(TableOrSubQuery node);
}
