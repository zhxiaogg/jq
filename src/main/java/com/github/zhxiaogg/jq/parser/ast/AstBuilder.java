package com.github.zhxiaogg.jq.parser.ast;

public interface AstBuilder<T extends AstNode> {
    T build();
}
