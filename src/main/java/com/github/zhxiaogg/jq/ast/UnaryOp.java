package com.github.zhxiaogg.jq.ast;

import lombok.Data;

@Data
public class UnaryOp implements AstNode {
    private final String op;
}
