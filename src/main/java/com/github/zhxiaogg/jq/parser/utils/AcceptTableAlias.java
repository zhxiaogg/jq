package com.github.zhxiaogg.jq.parser.utils;

import com.github.zhxiaogg.jq.ast.TableAlias;

interface AcceptTableAlias {
    void accept(TableAlias tableAlias);
}
