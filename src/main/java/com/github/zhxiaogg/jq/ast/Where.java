package com.github.zhxiaogg.jq.ast;

import lombok.Data;

@Data
public class Where implements AstNode {
    private final Expr expr;
}
