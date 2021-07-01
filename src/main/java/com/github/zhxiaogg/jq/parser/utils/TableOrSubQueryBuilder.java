package com.github.zhxiaogg.jq.parser.utils;

import com.github.zhxiaogg.jq.ast.TableOrSubQuery;

interface TableOrSubQueryBuilder<T extends TableOrSubQuery> extends AstBuilder<T> {
    @Override
    T build();
}
