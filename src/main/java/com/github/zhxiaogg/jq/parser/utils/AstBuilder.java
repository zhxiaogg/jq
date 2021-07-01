package com.github.zhxiaogg.jq.parser.utils;

import com.github.zhxiaogg.jq.ast.AstNode;

interface AstBuilder<T extends AstNode> {
    T build();
}
