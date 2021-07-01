package com.github.zhxiaogg.jq.parser.utils;

import com.github.zhxiaogg.jq.ast.ResultColumn;

interface ResultColumnBuilder<T extends ResultColumn> extends AstBuilder<T> {
    @Override
    public T build();
}
