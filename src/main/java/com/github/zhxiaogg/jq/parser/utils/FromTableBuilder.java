package com.github.zhxiaogg.jq.parser.utils;

import com.github.zhxiaogg.jq.ast.FromTable;

interface FromTableBuilder<T extends FromTable> extends AstBuilder<T> {
    @Override
    T build();
}
